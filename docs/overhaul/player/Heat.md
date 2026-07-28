Heat is an overhaul to fire damage to play well with the new [[Health & Damage Overhaul]] system.

Heat accrues from 0 to 100, and once it hits 100, it resets to 75 and deals one heart of true damage.

Your heat is shown by a shimmering heat overlay on your hearts and a warm vignette around the screen. These effects intensify as heat gets closer to 100.
When >= 75 heat, the top-most heart is visibly ablaze, and the vignette gains searing lines.

Heat is gained from the following sources:
- Standing on magma blocks accrues 1 heat per tick (5 seconds to burn)
- Crouching on magma blocks accrues 1 heat per tick, but caps out at 70 heat.
- Being close to lava accrues heat per tick scaling with how close you are, capping out at 70 heat for being as close as possible without being in it.
- Being on fire accrues 0.25 heat per tick, and instantly floors your heat at 80.
- Being in lava accrues 0.75 heat per tick, and instantly floors your heat at 80.
	- Supposed to stack with being on fire
- Being in [[Boiling Water]] accrues 0.5 heat per tick, and instantly floors your heat at 80.
- Being in the lower caverns of [[The Below]] accrues 0.1 heat per tick, but caps out at 50 heat.
- Taking damage from [[Blaze Bronze]] accrues 20 heat.

When not gaining heat from any sources, heat passively drops at 0.5 heat per tick.

Having the fire resistance effect caps your heat at 0.

Heat is the direct inverse of [[Freeze]]. You cannot be both freezing and overheating. Gaining freeze will drop heat, and you cannot actually start freezing until your heat is at 0. Same with vice-versa.
