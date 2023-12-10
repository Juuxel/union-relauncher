# Union Relauncher

A main class wrapper for Minecraft Forge that bundles multiple file paths to one mod's
contents into one URL.

It uses the original `MOD_CLASSES` environment variable present in Forge for
Minecraft ≤1.19.2 with the same format to support build tools without major changes.

## Usage

1. Set the `unionRelauncher.mainClass` system property to point to the original main class.
2. Set up the `MOD_CLASSES` environment variable with an id-path list.
   - The environment variable is a list of entries separated by `File.pathSeparator`.
   - Each entry is one of:
     - a file path belonging to the *default mod*
     - an id-path pair `id%%path`, where `id` is a unique id for the mod path group
       and `path` is the file path belonging to that mod
3. Launch the Union Relauncher module or jar.
