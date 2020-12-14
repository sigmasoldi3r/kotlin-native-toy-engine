# Horizon Engine

Argochamber's base game engine.

## Directions

Past attempts of achieving a stable product by combining `C++` with `Kotlin Native`
have failed due to one basic thing: _Memory management_.

For that reason, some basic rules must be enforced:

- All long lifetime objects must be owned by Kotlin in order to allow the GC to work with them.
  - Objects always must be trivial, with no destructors.
  - Complex objects can be hidden by storing them in an appropriate byte array, owned by Kotlin GC.
- When passing complex data to the unmanaged environment, either:
  - Pass by reference, never copy.
  - Or read directly from disk.
- Lifetime boundaries cannot be crossed.
    - This will prevent accessing invalid data, as each environment owns its own data.
- Avoid at all costs unmanaged long lifetime object creation (In either side).
  - All long lifetime objects (Non function local) **must** exist in Kotlin and be owned by the GC.

Other rules might arise with time.
