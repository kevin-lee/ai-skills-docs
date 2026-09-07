---
sidebar_position: 5
id: misc
title: Miscellaneous
---

# Miscellaneous

## Source Metadata

When you install a skill using ai-skills, a `.aiskills.json` file is automatically created inside the skill directory. This file records where the skill was installed from:

```json
{
  "name" : null,
  "source" : "owner/repo",
  "sourceType" : "git",
  "repoUrl" : "https://github.com/owner/repo.git",
  "branch" : null,
  "authMethod" : "anonymous",
  "subpath" : "skills/my-skill",
  "localPath" : null,
  "installedAt" : "2026-04-01T12:00:00Z"
}
```

This metadata enables the [`update`](commands/update.md) command to re-fetch skills from their original source. You do not need to create or edit this file manually - it is managed by ai-skills.

### Field Reference

| Field         | Meaning                                                                                              |
|---------------|------------------------------------------------------------------------------------------------------|
| `name`        | The skill name recorded at install time. `null` in records written before the field existed          |
| `source`      | Origin reference, e.g. `owner/repo` for git, a path for local                                        |
| `sourceType`  | Origin type: `git` or `local`                                                                        |
| `repoUrl`     | Clone URL, stored in canonical https form for `github.com`                                           |
| `branch`      | The [named branch](commands/install.md#named-branches) being tracked. `null` means default-branch tracking |
| `authMethod`  | The [authentication method](git-authentication.md#recorded-method) that last succeeded, tried first on the next update. `null` runs the full chain |
| `subpath`     | Path within the source where `SKILL.md` lives. `null` means the repository root                      |
| `localPath`   | The source directory, for `local` installs                                                           |
| `installedAt` | ISO-8601 timestamp of the install                                                                    |

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
