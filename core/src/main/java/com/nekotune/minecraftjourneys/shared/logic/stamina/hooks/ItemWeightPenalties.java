package com.nekotune.minecraftjourneys.shared.logic.stamina.hooks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.function.Consumer;

import com.nekotune.minecraftjourneys.MJConfig;
import com.nekotune.minecraftjourneys.MinecraftJourneys;
import com.nekotune.minecraftjourneys.shared.logic.stamina.PlayerStamina;
import com.nekotune.minecraftjourneys.shared.logic.stamina.PlayerStamina.StaminaFlag;
import com.nekotune.minecraftjourneys.shared.logic.stamina.StaminaEvent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = MinecraftJourneys.MOD_ID)
public final class ItemWeightPenalties {

    /**
     * Returns whether or not the given item stack should apply a weight penalty.
     * 
     * @param stack The item stack to check
     * @return True if the item stack should apply a weight penalty; false
     *         otherwise.
     */
    public static boolean isHeavy(ItemStack stack) {
        return stack.getItem() instanceof BundleItem
                && BundleItem.getFullnessDisplay(stack) > 0f;
    }

    /**
     * Searches a player's inventory for items that have weight penalties.
     * 
     * @param inventory The player's inventory to search through.
     * @return List of items that should apply weight flags.
     */
    public static InventoryQueryResult queryHeavyItems(final Inventory inventory) {
        final var queryResult = new InventoryQueryResult();
        final Consumer<ItemStack> tryAdd = stack -> {
            if (stack.isEmpty())
                return;
            if (isHeavy(stack)) {
                queryResult.add(stack);
            }
        };
        inventory.items.forEach(tryAdd);
        inventory.armor.forEach(tryAdd);
        inventory.offhand.forEach(tryAdd);
        return queryResult;
    }

    /**
     * Apply weight penalties to stamina cycles.
     */
    @SubscribeEvent
    public static void onStaminaCyclePre(StaminaEvent.TickEvent.CycleEvent.Pre event) {
        final double penaltyPerItem = MJConfig.HEAVY_ITEM_STAMINA_PENALTY.get();
        if (penaltyPerItem == 0d) return;
        WeightFlag.get(event.getStamina()).ifPresent(flag -> {
            final int penaltyLevel = flag.getPenaltyLevel();
            if (penaltyLevel == 0)
                return;

            // TODO: Remove debug logging
            if (event.getPlayer().level().getGameTime() % 40 == 0) {
                MinecraftJourneys.LOGGER.debug("[ItemWeightPenalties#onStaminaCyclePre] Penalty level: " + penaltyLevel
                        + "; Regen multiplier: " + Math.pow(penaltyPerItem, penaltyLevel) + "; Drain multiplier: "
                        + Math.pow(penaltyPerItem, -penaltyLevel));
            }

            event.multiplyIfRegen((float) Math.pow(penaltyPerItem, penaltyLevel));
            event.multiplyIfDrain((float) Math.pow(penaltyPerItem, -penaltyLevel));
        });
    }

    /**
     * Update players' stamina weight flags every N ticks.
     */
    @SubscribeEvent
    public static void onStaminaTickPost(final StaminaEvent.TickEvent.Post event) {
        final Player player = event.getPlayer();
        if (player.level().getGameTime() % 40 != 0)
            return;
        final PlayerStamina stamina = event.getStamina();
        final InventoryQueryResult heavyItems = queryHeavyItems(player.getInventory());
        if (heavyItems.count() == 0) {
            WeightFlag.tryRemove(stamina);
        } else {
            WeightFlag.set(stamina, heavyItems);
        }
    }

    public static final class InventoryQueryResult {
        private final HashMap<Integer, Integer> itemCounts = new HashMap<>();
        private final ArrayList<ItemStack> items = new ArrayList<>();
        private int n = 0;

        private InventoryQueryResult() {
        }

        public int count(final Item item) {
            return itemCounts.getOrDefault(Item.getId(item), 0);
        }

        public int count() {
            return n;
        }

        public void add(final ItemStack stack) {
            items.add(stack);
            increment(stack.getItem());
        }

        /**
         * @return The list of ItemStacks included in the query results.
         */
        public List<ItemStack> getItemStacks() {
            return items;
        }

        /**
         * @return A list of distinct Items included in the query results.
         */
        public List<Item> getItems() {
            return items.stream().map(stack -> {
                return stack.getItem();
            }).toList();
        }

        private void increment(final Item item) {
            final int count = itemCounts.computeIfAbsent(Item.getId(item), $ -> 0);
            itemCounts.put(Item.getId(item), count + 1);
            n++;
        }
    }

    public static final class WeightFlag extends StaminaFlag {
        private static final WeakHashMap<PlayerStamina, Optional<WeightFlag>> ACTIVE = new WeakHashMap<>();
        private InventoryQueryResult queryResult;
        private long lifeTime = 0;

        private WeightFlag(final PlayerStamina adornee,
                final InventoryQueryResult queryResult) {
            super(adornee);
            this.queryResult = queryResult;
        }

        /**
         * Returns the active WeightFlag attached to the given player's
         * stamina manager, if one exists.
         * 
         * @return Weak WeightFlag reference.
         */
        public static Optional<WeightFlag> get(final PlayerStamina adornee) {
            return ACTIVE.getOrDefault(adornee, Optional.empty());
        }

        /**
         * Sets the active weight flag for the given stamina manager.
         * 
         * @param adornee The stamina manager to attach the flag to.
         * @param item    The query of items inflicting this weight flag.
         * @return The active weight flag.
         */
        private static void set(final PlayerStamina adornee,
                final InventoryQueryResult queryResult) {
            get(adornee).ifPresentOrElse(flag -> {
                flag.queryResult = queryResult;
            }, () -> {
                ACTIVE.put(adornee, Optional.of(
                        new WeightFlag(adornee, queryResult)));
            });
        }

        /**
         * Removes the active weight flag for the given
         * stamina manager, if there was one.
         * 
         * @param adornee The stamina manager to remove the flag from.
         * @return Weak reference to the removed flag, if there was one.
         */
        public static Optional<WeightFlag> tryRemove(final PlayerStamina adornee) {
            final Optional<WeightFlag> flag = get(adornee);
            if (flag.isPresent()) {
                flag.get().remove();
            }
            return flag;
        }

        public long lifeTime() {
            return lifeTime;
        }

        public InventoryQueryResult getInflicting() {
            return queryResult;
        }

        public int getPenaltyLevel(Item item) {
            return Math.max(0, queryResult.count(item) - 1);
        }

        public int getPenaltyLevel() {
            int level = 0;
            for (final Item item : queryResult.getItems()
                    .stream().distinct().toList()) {
                level += getPenaltyLevel(item);
            }
            return level;
        }

        @Override
        protected void tick() {
            super.tick();
            this.lifeTime++;
        }

        @Override
        protected void onRemove() {
            super.onRemove();
            ACTIVE.put(adornee, Optional.empty());
        }
    }
}
