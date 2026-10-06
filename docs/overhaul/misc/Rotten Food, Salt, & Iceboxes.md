
**Rotten Food**

Food now decays over time. Decay is shown by a little orb in the top right as well as a moldy green/gray/brown texture overlay that slowly becomes more and more opaque.
Food slightly loses hunger restoration as it decays.

A food item takes 20 minutes to fully decay by default. Some food items rot faster, some slower, and some food items don't even decay at all.
Some examples:
- Apples take 20 minutes to fully decay
- Raw chicken takes 10 minutes to fully decay
- Honey does not decay

Combining two item stacks with different decay values creates a new item stack with a decay value that is the average of both, weighted towards whichever stack was bigger. This also leans towards the decayed value, so combining a half-decayed apple with a fresh apple will give a stack of two 5/16ths decayed apples.

After fully decaying, food turns into a new **rot** item. This item has a compost chance of 100%.
Food in bowls turn into a new bowl of rot, which can be placed in a crafting grid to extract the rot, leaving the empty bowl in the crafting grid.


**Salt**

Salt is a new resource found in dripstone caves and in new salt caves biomes which only generate under oceans.

New block, dripsalt, which is basically just dripstone for salt. Stalactites of dripsalt with water above them create stalagmites of dripsalt below them over long periods of time. Dripsalt can be ground up in a mortar for 1 salt.

Salt can be applied to any bowl food item to extend its decay timer by 2x and to increase its food values by +1 shank and +1 saturation. If the food item grants any positive effects, those effects last 10% longer.

To apply salt to a bowl food item, just right-click the item with salt in your cursor, or craft it together in your inventory crafting UI.

Salted foods have a little "salted" line in their tooltip.

You can put raw meat on a drying rack to create jerky. This process takes 20 minutes. Salted raw meat dries 4x faster, drying out in 5 minutes.
Jerky does not decay.
Jerky gives very little hunger value.


**Iceboxes**

Food takes 10x longer to rot when stored in an Icebox, a new variant of chests which requires a constant fuel of ice blocks and other cold blocks. An icebox is crafted with 8 quartz surrounding a breeze rod.

Iceboxes have a 3x3 grid of slots to store items, for a total of 9 slots. The input slot for fuel is on the left side of the GUI next to the grid.

Like chests, iceboxes can be placed next to each other to form double iceboxes. They can also uniquely be placed on top of each other to form vertical double iceboxes which resemble refrigerators.
- Double iceboxes have 6 rows of 3 slots to store items, for a total of 18 slots.
- Double iceboxes burn through fuel twice as fast, but have two slots to store fuel in instead of just one.

While fueled, iceboxes convert some blocks and items:
- Magma blocks are converted into cobblestone
- Lava buckets are converted into "stone buckets" which craft into equal parts stone to the amount of lava previously stored, leaving behind the empty bucket
Any items that are converted by iceboxes will quiver while inside of them and will have a tiny progress bar above them showing how close they are to converting.

Iceboxes can be fueled by:
- Ice - fuels for 2 minutes per block
- Packed ice - fuels for 10 minutes per block
- Blue ice - fuels for 30 minutes per block
- Breeze rod - fuels for 2 minutes per rod
- Wind charge - fuels for 30 seconds per wind charge
- Snow - fuels for 1 minute per block
- Snowball - fuels for 30 seconds per snowball
- Bucket of Powdered Snow - fuels for 1 minute, then leaves behind the empty bucket in the fuel slot