---
sidebar_position: 5
id: misc
title: Miscellaneous
---

# Miscellaneous

## Source Metadata

When you install a skill using ai-skills, a `.aiskills.json` file is automatically created inside the skill directory. This file records where the skill was installed from and which version was installed:

```json
{
  "name" : null,
  "source" : "owner/repo",
  "sourceType" : "git",
  "repoUrl" : "https://github.com/owner/repo",
  "branch" : null,
  "authMethod" : "anonymous",
  "subpath" : "skills/my-skill",
  "localPath" : null,
  "commit" : "3f9c2a1d8e7b6c5a4f3e2d1c0b9a8f7e6d5c4b3a",
  "sourceHash" : "5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2c3d4e",
  "installedHash" : "5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2c3d4e",
  "installedAt" : "2026-09-27T12:00:00.000Z",
  "checkedAt" : "2026-09-28T09:30:00.000Z"
}
```

This metadata enables the [`update`](commands/update.md) command to check skills against their original source and re-fetch the ones that changed. You do not need to create or edit this file manually - it is managed by ai-skills.

### Field Reference

| Field           | Meaning                                                                                              |
|-----------------|------------------------------------------------------------------------------------------------------|
| `name`          | The skill name recorded at install time. `null` in records written before the field existed          |
| `source`        | Origin reference, e.g. `owner/repo` for git, a path for local                                        |
| `sourceType`    | Origin type: `git` or `local`                                                                        |
| `repoUrl`       | Clone URL, stored in canonical https form for `github.com` (`https://github.com/owner/repo`)         |
| `branch`        | The [named branch](commands/install.md#named-branches) being tracked. `null` means default-branch tracking |
| `authMethod`    | The [authentication method](git-authentication.md#recorded-method) that last succeeded, tried first on the next update. `null` runs the full chain |
| `subpath`       | Path within the source where `SKILL.md` lives. `null` means the repository root                      |
| `localPath`     | The source directory, for `local` installs                                                           |
| `commit`        | The Git commit the installed content came from. Git sources only, for display                        |
| `sourceHash`    | The source version: the Git tree hash of the source skill folder. `null` means not recorded          |
| `installedHash` | The Git tree hash of the installed files when ai-skills last wrote them. `null` means not recorded   |
| `installedAt`   | ISO-8601 timestamp of when ai-skills last installed or updated the skill's files                     |
| `checkedAt`     | When [`update`](commands/update.md) last checked the skill. Written for global skills only           |

### Version Hashes

`sourceHash` and `installedHash` are Git tree hashes, the value Git records for a folder, so ai-skills computes them with `git`.

- For a Git source, `sourceHash` is the tree hash of the skill's folder at the fetched commit (`git rev-parse HEAD:<subpath>`).
- For a local source, and for `installedHash`, the folder is hashed as it is on disk. The top-level `.aiskills.json`, `.DS_Store`, `Thumbs.db` and `desktop.ini` at any depth, and `.git` are left out. Line endings are normalized, so CRLF and LF copies hash the same.
- `installedHash` is recomputed only when ai-skills writes the skill's files. If the files no longer match it, [`update`](commands/update.md#local-changes) treats the skill as locally changed.
- A source folder that contains a symbolic link has no version, because the link may point outside the folder. Without `git`, no version is recorded either. Such skills are updated on every run.

`commit`, `sourceHash`, `installedHash` and `checkedAt` are `null` in records written by 2.10.0 or earlier. The next update records them, so no migration is needed.

### Repo-Root Skills

`""`, `"."`, and whitespace-only subpaths all mean "repository root" and are canonicalized to
`null`. Legacy `.aiskills.json` files already on disk are normalized when they are read, so no
migration is needed. [`list`](commands/list.md) and [`read`](commands/read.md) display such skills
as `<root>` rather than as an empty or `.` subpath.

:::info
Skills installed before source metadata tracking was added will not have a `.aiskills.json` file. Re-install them once to enable updates.
:::

## Temporary Files

ai-skills creates short-lived working directories during operations like `install` and `update` (for example, when cloning a Git repository). These are placed under the system's temporary directory - `$TMPDIR` if set, falling back to `/tmp` - instead of your home directory.

ai-skills removes these directories automatically when the operation finishes. Anything left behind by an unexpected exit is also cleared by the operating system on reboot, since the system temporary directory is ephemeral.
