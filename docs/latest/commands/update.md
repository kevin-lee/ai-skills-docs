---
sidebar_position: 5
id: update
title: update
---

# update

Update installed skills by re-fetching them from their original source.

## Synopsis

```bash
aiskills update [skill-names]
```

## Usage

### Update All Skills

```bash
aiskills update
```

Updates every installed skill that has source metadata. Skills from the same Git repository **and branch** are grouped and cloned only once.

### Update Specific Skills

```bash
aiskills update commit                       # Update a single skill
aiskills update commit review-pr             # Update specific skills
```

## How It Works

When you install a skill, ai-skills records where it came from in a `.aiskills.json` metadata file inside the skill directory. The `update` command uses this metadata to:

1. **Git sources** - clone the original repository, extract the skill, and replace the installed copy. The clone uses the [Git authentication](../git-authentication.md) fallback chain.
2. **Local sources** - copy from the original local directory path.

Skills from the same repository are grouped together so the repository is cloned only once, even if multiple skills were installed from it.

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
- Accepting clears the recorded branch only for the skills that were actually updated from the
  default branch.
- Switching to the default branch changes local tracking metadata only. It never deletes a remote
  branch.

Retained branches are counted in the summary and listed separately:

```
Summary: 2 updated, 1 skipped (3 total)
Missing branch - selection retained (1): my-skill
```

## Safe Replacement

A Git update is staged next to the target first. The new version is copied, renamed, and given its
metadata in a temporary staging directory, and only then swapped into place. If the swap fails, the
backup is moved back, and the outcome is reported precisely:

```
Skipped: my-skill (Replacement failed: ...)
```

If even the rollback fails, the message names the backup path so the installation can be recovered
by hand. Staging directories are removed in every case except that one.

## Skipped Skills

The following skills are skipped during update:

| Reason                                         | What to do                                                     |
|------------------------------------------------|----------------------------------------------------------------|
| No source metadata (`.aiskills.json` missing)  | Re-install the skill once to enable updates                    |
| Local source path no longer exists             | Restore the source directory or re-install from a new location |
| `SKILL.md` missing at the recorded source path | Check that the source repository still contains the skill      |
| Missing repo URL in metadata                   | Re-install the skill                                           |
| Git clone failed                               | Check network connectivity and repository access               |
| Recorded branch no longer exists               | Accept the switch to the default branch, or re-install with a branch that exists |
| Replacement failed                             | The skill was restored from its backup. Re-run `update`, or recover from the named backup path |

## Output

After updating, a summary is displayed:

```
✅ Updated: commit (project, Claude): .claude/skills
✅ Updated: review-pr (project, Claude): .claude/skills
Summary: 2 updated, 0 skipped (2 total)
```

If any skills were skipped, the reasons are listed:

```
Summary: 1 updated, 1 skipped (2 total)
Missing source metadata (1): old-skill
Re-install these skills once to enable updates (e.g., `aiskills install <source>`).
```
