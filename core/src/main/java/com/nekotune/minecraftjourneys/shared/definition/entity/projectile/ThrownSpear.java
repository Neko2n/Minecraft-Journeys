package com.nekotune.minecraftjourneys.shared.definition.entity.projectile;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import com.nekotune.minecraftjourneys.shared.registry.audio.MJSoundEvents;
import com.nekotune.minecraftjourneys.shared.registry.content.MJEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ThrownSpear extends AbstractArrow implements ItemSupplier {

    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(ThrownSpear.class,
            EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(ThrownSpear.class,
            EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(ThrownSpear.class,
            EntityDataSerializers.ITEM_STACK);
    private boolean dealtDamage;
    public int clientSideReturnSpearTickCount;

    private final Set<Entity> stickingEntities = new HashSet<>();
    private final Set<Entity> floatingEntities = new HashSet<>(); // Sticking entities which had their gravity disabled
    private static final Set<Entity> RESERVED_ENTITIES = new HashSet<>(); // Prevent multiple spears from sticking the
                                                                          // same entity

    private final float throwDamage;

    public ThrownSpear(EntityType<? extends ThrownSpear> entityType, Level level) {
        super(entityType, level);
        this.throwDamage = 2.0F;
    }

    public ThrownSpear(float throwDamage, Level level, double x, double y, double z, ItemStack pickupItemStack) {
        super(MJEntities.THROWN_SPEAR.get(), x, y, z, level, pickupItemStack, pickupItemStack);
        this.entityData.set(ID_LOYALTY, this.getLoyaltyFromItem(pickupItemStack));
        this.entityData.set(ID_FOIL, pickupItemStack.hasFoil());
        this.entityData.set(DATA_ITEM_STACK, pickupItemStack.copy());
        this.throwDamage = throwDamage;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ID_LOYALTY, (byte) 0);
        builder.define(ID_FOIL, false);
        builder.define(DATA_ITEM_STACK, ItemStack.EMPTY);
    }

    private boolean stickEntity(Entity target) {
        if (ThrownSpear.RESERVED_ENTITIES.contains(target))
            return false;
        ThrownSpear.RESERVED_ENTITIES.add(target);
        stickingEntities.add(target);
        if (!target.isNoGravity()) {
            floatingEntities.add(target);
            target.setNoGravity(true);
        }
        return true;
    }

    @Override
    public void onRemovedFromLevel() {
        floatingEntities.forEach(entity -> {
            if (entity.isRemoved())
                return;
            entity.setNoGravity(false);
        });
        floatingEntities.clear();
        stickingEntities.forEach(entity -> {
            ThrownSpear.RESERVED_ENTITIES.remove(entity);
        });
        stickingEntities.clear();
        super.onRemovedFromLevel();
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        // Update sticking entities' positions
        stickingEntities.forEach(entity -> {
            if (entity.isRemoved())
                return;
            entity.setPos(this.position());
            entity.setDeltaMovement(Vec3.ZERO);
        });

        Entity entity = this.getOwner();
        int i = this.entityData.get(ID_LOYALTY);
        if (i > 0 && (this.dealtDamage || this.isNoPhysics()) && entity != null) {
            if (!this.isAcceptibleReturnOwner()) {
                if (!this.level().isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }

                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 vec3 = entity.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015 * (double) i, this.getZ());
                if (this.level().isClientSide) {
                    this.yOld = this.getY();
                }

                double d0 = 0.05 * (double) i;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add(vec3.normalize().scale(d0)));
                if (this.clientSideReturnSpearTickCount == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }

                this.clientSideReturnSpearTickCount++;
            }
        }

        super.tick();
    }

    private boolean isAcceptibleReturnOwner() {
        Entity entity = this.getOwner();
        return entity == null || !entity.isAlive() ? false : !(entity instanceof ServerPlayer) || !entity.isSpectator();
    }

    public boolean isFoil() {
        return this.entityData.get(ID_FOIL);
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        if (target instanceof ItemEntity item)
            return !stickingEntities.contains(item);
        return super.canHitEntity(target);
    }

    /**
     * Gets the EntityHitResult representing the entity hit
     */
    @Nullable
    @Override
    protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
        return this.dealtDamage ? null : super.findHitEntity(startVec, endVec);
    }

    /**
     * Called when the arrow hits an entity
     */
    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();

        // Grabs onto items
        if (entity instanceof ItemEntity item) {
            this.stickEntity(item);

        } else {

            // Double damage to fish-type entities
            final boolean isFish = entity instanceof AbstractFish fish && fish.attackable();
            float f = this.throwDamage;
            if (isFish)
                f *= 2.0F;

            // Calculate damage
            Entity entity1 = this.getOwner();
            DamageSource damagesource = this.damageSources().thrown(this, (Entity) (entity1 == null ? this : entity1));
            if (this.level() instanceof ServerLevel serverlevel) {
                f = EnchantmentHelper.modifyDamage(serverlevel, this.getWeaponItem(), entity, damagesource, f);
            }

            // Attempt to apply damage
            this.dealtDamage = true;
            if (entity.hurt(damagesource, f)) {
                if (entity.getType() == EntityType.ENDERMAN) {
                    return;
                }
                if (this.level() instanceof ServerLevel serverlevel1) {
                    EnchantmentHelper.doPostAttackEffectsWithItemSource(serverlevel1, entity, damagesource,
                            this.getWeaponItem());
                }
                if (entity instanceof LivingEntity livingentity) {
                    this.doKnockback(livingentity, damagesource);
                    this.doPostHurtEffects(livingentity);
                }
            }

            // Play the entity hit sound
            final float fv = 0.5F;
            final float pitch = random.nextFloat() * fv + 1.0F - (fv / 2.0F);
            this.playSound(MJSoundEvents.SPEAR_HIT.get(), 1.0F, pitch);
        }

        // By default, block hits are ignored on ticks when entities are hit
        Vec3 start = this.position();
        Vec3 end = start.add(this.getDeltaMovement());
        BlockHitResult blockHit = this.level()
                .clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (blockHit.getType() != HitResult.Type.MISS) {
            this.onHitBlock(blockHit);
        }
    }

    @Override
    protected void doPostHurtEffects(LivingEntity target) {
        if (target.isAlive() || target.getBoundingBox().getSize() >= 1.0D) {
            // Only bounce if it didn't kill the target, or if the target is large
            this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
        } else {
            // Otherwise, they stick to the spear and the spear continues traveling
            this.stickEntity(target);
        }
        super.doPostHurtEffects(target);
    }

    @Override
    protected void hitBlockEnchantmentEffects(ServerLevel level, BlockHitResult hitResult, ItemStack stack) {
        Vec3 vec3 = hitResult.getBlockPos().clampLocationWithin(hitResult.getLocation());
        EnchantmentHelper.onHitBlock(
                level,
                stack,
                this.getOwner() instanceof LivingEntity livingentity ? livingentity : null,
                this,
                null,
                vec3,
                level.getBlockState(hitResult.getBlockPos()),
                p_348680_ -> this.kill());
    }

    @Override
    public ItemStack getWeaponItem() {
        return this.getPickupItemStackOrigin();
    }

    @Override
    public ItemStack getItem() {
        return this.entityData.get(DATA_ITEM_STACK);
    }

    @Override
    protected boolean tryPickup(Player player) {
        return super.tryPickup(player)
                || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return ItemStack.EMPTY;
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    @Override
    public void playerTouch(Player entity) {
        if (this.ownedBy(entity) || this.getOwner() == null) {
            super.playerTouch(entity);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.dealtDamage = compound.getBoolean("DealtDamage");
        this.entityData.set(ID_LOYALTY, this.getLoyaltyFromItem(this.getPickupItemStackOrigin()));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("DealtDamage", this.dealtDamage);
    }

    private byte getLoyaltyFromItem(ItemStack stack) {
        return this.level() instanceof ServerLevel serverlevel
                ? (byte) Mth.clamp(EnchantmentHelper.getTridentReturnToOwnerAcceleration(serverlevel, stack, this), 0,
                        127)
                : 0;
    }

    @Override
    public void tickDespawn() {
        int i = this.entityData.get(ID_LOYALTY);
        if (this.pickup != AbstractArrow.Pickup.ALLOWED || i <= 0) {
            super.tickDespawn();
        }
    }

    @Override
    protected float getWaterInertia() {
        return 0.8F;
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return MJSoundEvents.SPEAR_HIT_GROUND.get();
    }
}
