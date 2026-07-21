package com.nekotune.minecraftjourneys.mixin.minecraft;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;

@Mixin(AnvilMenu.class)
public final class AnvilMenuMixin {
	@Shadow @Final private DataSlot cost;

	@Inject(method = "mayPickup", at = @At("HEAD"), cancellable = true)
	private void noxpanvils$mayPickup(Player player, boolean present, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(true);
	}
	@Inject(method = "onTake", at = @At("HEAD"))
	private void noxpanvils$onTake(Player player, ItemStack stack, CallbackInfo ci) {
	    this.cost.set(0);
	}

	@Inject(method = "createResult", at = @At("TAIL"))
	private void noxpanvils$updateResult(CallbackInfo ci) {
		this.cost.set(0);
	}

	@Redirect(method = {"createResult"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getOrDefault(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;"))
	private <T> T noxpanvils$getOrDefault(ItemStack instance, DataComponentType<T> dataComponentType, T o) {
		if (dataComponentType == DataComponents.REPAIR_COST) return o;
		return instance.getOrDefault(dataComponentType, o);
	}

	@Redirect(method = {"createResult"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/DataSlot;get()I"))
	private int noxpanvils$getItemLevelCost(DataSlot instance) {
		return 0;
	}

	@Redirect(method = {"createResult"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;set(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;"))
	private <T> T noxpanvils$setRepairCost(ItemStack instance, DataComponentType<T> type, T value) {
		if (type == DataComponents.REPAIR_COST) return null;
		return instance.set(type, value);
	}

	@Inject(method = "getCost", at = @At("HEAD"), cancellable = true)
	private void noxpanvils$getCost(CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(0);
	}
}
