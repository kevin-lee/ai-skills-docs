---
sidebar_position: 4
id: git-authentication
title: Git Authentication
---

# Git Authentication

`install`, `update`, and `search` all clone through the same logic. For a `github.com` repository,
in either URL form, it escalates through the available authentication methods instead of giving up
after the first failure.

## The Fallback Chain

| Order | Method              | What it does                                                       |
|-------|---------------------|--------------------------------------------------------------------|
| 1     | `anonymous`         | https with credential helpers disabled (public repos)              |
| 2     | `ssh`               | `git@github.com:owner/repo.git` (may prompt for a key passphrase)  |
| 3     | `gh`                | `gh` used as a git credential helper                               |
| 4     | `credential-helper` | https with the configured git credential helper                    |
| 5     | `interactive`       | asks first, then hands the terminal to git for its own prompt      |

## Failure Report

The failure is only reported after every method has been tried, and the report says what was
attempted:

```
  ✖ Clone failed
  anonymous: remote: Repository not found.
  ssh: git@github.com: Permission denied (publickey).
  gh: skipped (gh not found on PATH)
  credential-helper: skipped (no git credential helper configured)
SSH, gh, and credential helper access all failed or are unavailable.
? Try https clone with username/password? (git will prompt, use a personal access token as the password) ›
  ‣ Yes          - git will prompt for username/password
    No           - abort
```

## Interactive Last Resort

The interactive step only runs when stdin is a TTY and `--yes` was not passed, so CI and piped runs
degrade to the non-interactive steps and then fail with the report instead of hanging.

ai-skills never reads or stores credentials itself - git does its own prompting, and a personal
access token works as the password.

If you decline, or no interactive step is available, a closing tip is printed:

```
Tip: For private repos, set up an SSH key, run `gh auth login`, or configure a git credential helper
```

## Recorded Method

`.aiskills.json` carries an optional `authMethod` field recording the method that succeeded.

[`update`](commands/update.md) moves that method to the front of the chain rather than replacing the
chain, then rewrites the metadata with whichever method actually won. Metadata without `authMethod`
simply runs the full chain, so no migration is needed.

## Repository URLs

`repoUrl` is always stored in canonical https form for `github.com`, so it no longer flips between
ssh and https across updates. Legacy records holding an ssh URL are repaired on the next successful
update.

## Non-GitHub Remotes

Non-GitHub https URLs also gain the `credential-helper` and `interactive` steps. Ssh-form and
`git://` URLs keep the single-attempt behavior.

## See Also

- [`install`](commands/install.md#private-repositories) - installing from private repositories
- [`update`](commands/update.md) - re-fetching skills from their original source
- [Miscellaneous](misc.md#source-metadata) - the `.aiskills.json` field reference
