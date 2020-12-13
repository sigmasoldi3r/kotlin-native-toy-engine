# Horizon Engine

Argochamber's base game engine.

## Directions

Past attempts of achieving a stable product by combining `C++` with `Kotlin Native`
have failed due to one basic thing: _Memory management_.

For that reason, some basic rules must be enforced:

- Communication from unmanaged code (C/C++) will be done ONLY via
primitive types, excluding heaps (Plain C arrays).
    - This naturally forbids passing objects back and forth.
- Complex data structures will be communicated via object marshaling (Eg: Messagepack, JSON...).
- When complex data must be feed to the C/C++ environment, read from there when possible (Eg: Loading a texture, just
  read from the disk!).
- Lifetime boundaries cannot be crossed: Copy data when crossing the boundary between environments.
    - This will prevent accessing invalid data, as each environment owns its own data.

Other rules might arise with time.
