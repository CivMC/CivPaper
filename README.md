# CivPaper

CivPaper is a fork of 1.21.8 Paper with optimisations intended for survival-like servers with many entities and players, distributed widely.

It is a high priority that gameplay is not affected in any way, and this is intended as a kind of "staging ground" for merging into mainline Paper.

Currently, we have three patches:

- Optimises TemptGoal for animals so that they don't look at the items for every player every tick
- Optimises mob despawning to use a spatial data structure to find how far away a player is so that a mob can despawn
- Run random ticks less often (this does affect gameplay)
