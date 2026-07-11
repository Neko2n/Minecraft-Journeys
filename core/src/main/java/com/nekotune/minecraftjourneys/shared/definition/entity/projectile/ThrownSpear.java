package com.nekotune.minecraftjourneys.shared.definition.entity.projectile;

import java.util.Collection;
import java.util.HashSet;

import javax.annotation.Nullable;

import com.nekotune.minecraftjourneys.shared.registry.audio.MJSoundEvents;
import com.nekotune.minecraftjourneys.shared.registry.content.MJEntities;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
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
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

@EventBusSubscriber
public class ThrownSpear extends AbstractArrow implements ItemSupplier {

    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(ThrownSpear.class,
            EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(ThrownSpear.class,
            EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(ThrownSpear.class,
            EntityDataSerializers.ITEM_STACK);

    private boolean inGround = false;
    public int clientSideReturnSpearTickCount;
    public final Collection<Entity> hitEntities = new HashSet<>();
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

    @Override
    public void tick() {

        // Update loyalty recall state
        if (this.inGroundTime > 4) {
            this.inGround = true;
        }
        Entity entity = this.getOwner();
        int i = this.entityData.get(ID_LOYALTY);
        if (i > 0 && (this.inGround || this.isNoPhysics()) && entity != null) {
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
        if (target instanceof ItemEntity)
            return !getPassengers().contains(target);
        if (hitEntities.contains(target)) return false;
        return super.canHitEntity(target);
    }

    /**
     * Gets the EntityHitResult representing the entity hit
     */
    @Nullable
    @Override
    protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
        return this.inGround ? null : super.findHitEntity(startVec, endVec);
    }

    /**
     * Called when the arrow hits an entity
     */
    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        Level level = entity.level();

        // Grabs onto items
        if (entity instanceof ItemEntity item) {
            item.setNeverPickUp();
            item.startRiding(this);

            // VFX + SFX
            level.playSound(null, item.blockPosition(),
                    MJSoundEvents.SPEAR_HIT.get(), SoundSource.BLOCKS,
                    0.8f, 1.4f);
            if (level instanceof ServerLevel serverlevel) {
                serverlevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, item.getItem()),
                        item.getX(), item.getY() + item.getBbHeight() * 0.5, item.getZ(),
                        6, 0, 0, 0, 0.1);
            }

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
            this.hitEntities.add(entity);
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

    /**
     * Whether the spear should stick the hit entity to itself,
     * carrying it as a passenger.
     * @param target The entity the spear hit.
     */
    protected boolean shouldStick(LivingEntity target) {
        return !target.isAlive()
                && target.getBoundingBox().getSize() <= 1.0D;
    }

    @Override
    protected void doPostHurtEffects(LivingEntity target) {
        super.doPostHurtEffects(target);
        if (shouldStick(target)) {
            target.startRiding(this);
        } else if (hitEntities.size() <= this.getPierceLevel()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, 0.0, 1.0));
        } else {
            this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
        }
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
        this.inGround = compound.getBoolean("InGround");
        this.entityData.set(ID_LOYALTY, this.getLoyaltyFromItem(this.getPickupItemStackOrigin()));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("InGround", this.inGround);
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

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float partialTick) {
        return Vec3.ZERO;
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return passenger instanceof LivingEntity
                || passenger instanceof ItemEntity;
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        if (passenger instanceof ItemEntity item) {
            item.setDefaultPickUpDelay();
        }
    }

    @Override
    public void onRemovedFromLevel() {
        super.onRemovedFromLevel();
        this.hitEntities.clear();
    }

    /**
     * Spears grab the drops of entities they kill
     */
    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        Entity source = event.getSource().getDirectEntity();
        if (!(source instanceof ThrownSpear spear))
            return;
        for (ItemEntity drop : event.getDrops()) {
            drop.setNeverPickUp();
            drop.startRiding(spear);
        }
    }
}
