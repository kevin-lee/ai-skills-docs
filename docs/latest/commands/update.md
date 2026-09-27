---
sidebar_position: 5
id: update
title: update
---

# update

Update installed skills from their original source. A skill whose source has not changed is left untouched, and local edits are never overwritten unless you pass `--force`.

## Synopsis

```bash
aiskills update [skill-names] [options]
```

## Usage

### Update All Skills

```bash
aiskills update
```

Checks every installed skill that has source metadata and replaces only the ones whose source changed. Skills from the same Git repository **and branch** are grouped and cloned only once.

### Update Specific Skills

```bash
aiskills update commit                       # Update a single skill
aiskills update commit review-pr             # Update specific skills
aiskills update --force commit               # Update even if unchanged or locally edited
```

## Options

| Flag      | Short | Description                                                 |
|-----------|-------|-------------------------------------------------------------|
| `--force` | `-f`  | Update even when a skill is up to date or has local changes |

## How It Works

When you install a skill, ai-skills records where it came from and which version was installed in a `.aiskills.json` metadata file inside the skill directory. The `update` command uses this metadata to:

1. **Git sources** - clone the original repository and read the version of the skill's folder at the fetched commit. The clone uses the [Git authentication](../git-authentication.md) fallback chain.
2. **Local sources** - compute the version of the original local directory.
3. **Compare** - compare that version with the recorded one, and the installed files with their recorded state, then update the skill, leave it as it is, or keep its local changes. See [Version Check](#version-check).

Skills from the same repository are grouped together so the repository is cloned only once, even if multiple skills were installed from it.

A skill folder is always copied without `.git`, so a skill at the root of its repository does not become a Git repository of its own. See [Known Issues](../known-issues.md) for the problem this fixed in 2.11.0.

## Version Check

A skill's version is the Git tree hash of its own folder, not the repository commit. A commit that only changes other paths in the same repository does not make a skill out of date. The commit is recorded too, for display only.

`update` compares two things:

- the latest version of the source with the recorded `sourceHash`
- the installed files with the recorded `installedHash`, which ai-skills records whenever it writes the skill's files

| Source                  | Installed files           | Result                                        |
|-------------------------|---------------------------|-----------------------------------------------|
| Unchanged               | Unchanged or not recorded | **Up to date** - the files are left untouched |
| Changed or not recorded | Unchanged or not recorded | **Updated** - the skill is replaced           |
| Any                     | Edited locally            | **Local changes** - the edits are kept        |

With `--force`, every skill is **Updated** regardless of this table.

See [Version Hashes](../misc.md#version-hashes) for how the hashes are computed.

### Local Changes

A locally edited skill is never overwritten without `--force`. The result line says why it was kept:

| Detail                                              | Meaning                                  |
|-----------------------------------------------------|------------------------------------------|
| `source unchanged`                                  | Only the installed copy changed          |
| `source updated - use --force to overwrite`         | The source changed as well               |
| `source version unknown - use --force to overwrite` | The source version could not be computed |

To replace the edited copy with the source:

```bash
aiskills update --force <skill-name>
```

### Skills Without a Recorded Version

- Skills installed by 2.10.0 or earlier have no recorded version. Their next update replaces them and records one, so no migration is needed.
- A version cannot be recorded when Git is not available, or when the source folder contains a symbolic link, because the link may point outside the folder. Such a skill is updated on every run, as before, and listed in the summary under `Versions not recorded`.

### Timestamps

- `installedAt` changes only when the skill's files are written.
- `checkedAt` records when `update` last checked the skill, including checks that change nothing. It is written for global skills only. Project skills do not get it, because their directories are usually shared through the repository, and a per-machine timestamp would create a diff on every run.

## Branch Tracking

`update` groups installed skills by repository **and** branch, so two skills from the same
repository tracking different branches are cloned separately instead of both being updated from one
clone.

When Git confirms that a recorded branch is gone, an interactive `update` offers a choice:

```
Repository: https://github.com/owner/repo
  my-skill (Claude, global): ~/.claude/skills/my-skill
? Branch 'develop' no longer exists. Switch these installations to the repository's default branch for this and future updates? ›
  ‣ Keep branch - skip these updates
    Switch to default branch
```

- The prompt appears only when Git actually confirms the branch is missing. Authentication and
  network failures are reported as clone failures and never trigger a switch.
- Without terminal input (piped or CI runs), the affected updates are skipped and the branch
  selection is retained.
- Declining, or interrupting the prompt with Ctrl-C, also keeps the selection.
- Accepting records default-branch tracking for every skill that was checked against the default branch, whether it was updated, already up to date or kept because of local changes. A skill that fails to update keeps its recorded branch.
- When the content on the default branch is the same as the installed skill, the files are kept and the result line says `(now tracking the default branch)`.
- Switching to the default branch changes local tracking metadata only. It never deletes a remote
  branch.

A retained branch is reported on the skill's result line, counted as skipped in the summary, and listed separately:

```
🟥 Skipped:       my-skill (global, Claude): ~/.claude/skills (branch 'develop' missing - selection retained)
Summary: 2 updated, 0 up to date, 0 local changes, 1 skipped (3 total)
Missing branch - selection retained (1): my-skill
```

## Safe Replacement

A Git update is staged next to the target first. The new version is copied, renamed, and given its
metadata in a temporary staging directory, and only then swapped into place. If the swap fails, the
backup is moved back, and the outcome is reported precisely:

```
🟥 Skipped:       my-skill (global, Claude): ~/.claude/skills (Replacement failed: ...)
```

If even the rollback fails, the message names the backup path so the installation can be recovered
by hand. Staging directories are removed in every case except that one. Such skills are listed in the summary under `Update failed`.

## Skipped Skills

The following skills are skipped during update. Each gets a `🟥 Skipped:` result line with the reason:

| Reason                                         | What to do                                                     |
|------------------------------------------------|----------------------------------------------------------------|
| No source metadata (`.aiskills.json` missing)  | Re-install the skill once to enable updates                    |
| Local source path no longer exists             | Restore the source directory or re-install from a new location |
| `SKILL.md` missing at the recorded source path | Check that the source repository still contains the skill      |
| Missing repo URL in metadata                   | Re-install the skill                                           |
| Git clone failed                               | Check network connectivity and repository access               |
| Recorded branch no longer exists               | Accept the switch to the default branch, or re-install with a branch that exists |
| Replacement failed                             | The skill was restored from its backup. Re-run `update`, or recover from the named backup path |

Skills with local changes are not skipped. They are reported as **Local changes**, as described in [Local Changes](#local-changes).

## Output

Every skill gets one result line with a status, and a summary follows:

```
✅ Updated:       2020-hindsight-scala (global, Claude): ~/.claude/skills (1a2b3c4 → 5d6e7f8)
🟩 Up to date:    pr-analysis (global, Claude): ~/.claude/skills
🟨 Local changes: riper-5 (global, Claude): ~/.claude/skills (source unchanged)
🟨 Local changes: archify (global, Claude): ~/.claude/skills (source updated - use --force to overwrite)
🟥 Skipped:       foo (global, Claude): ~/.claude/skills (git clone failed)
Summary: 1 updated, 1 up to date, 2 local changes, 1 skipped (5 total)
Local changes (2): riper-5, archify
Run `aiskills update --force <skill-name>` to overwrite local changes.
Clone failed (1): foo
```

| Status             | Meaning                                                                          |
|--------------------|----------------------------------------------------------------------------------|
| ✅ `Updated`       | The source changed, or `--force` was used, so the skill was replaced             |
| 🟩 `Up to date`    | The source is unchanged, so the skill's files were left untouched                |
| 🟨 `Local changes` | The installed files were edited, so they were kept                               |
| 🟥 `Skipped`       | The skill could not be checked or updated. See [Skipped Skills](#skipped-skills) |

An updated skill's result line shows its version change:

| Detail                 | Meaning                                                                              |
|------------------------|--------------------------------------------------------------------------------------|
| `1a2b3c4 → 5d6e7f8`    | The short recorded version and the short new version                                 |
| `unrecorded → 5d6e7f8` | No version was recorded before, typically for a skill installed by 2.10.0 or earlier |
| `forced`               | `--force` replaced a skill whose source is unchanged                                 |
| `version unknown`      | The new version could not be computed                                                |

After the summary, skills that need attention are listed by reason, each with a count and the skill names, for example `Local changes`, `Versions not recorded`, `Missing source metadata`, `Missing branch - selection retained`, `Update failed` and `Clone failed`:

```
Summary: 1 updated, 0 up to date, 0 local changes, 1 skipped (2 total)
Missing source metadata (1): old-skill
Re-install these skills once to enable updates (e.g., `aiskills install <source>`).
```
