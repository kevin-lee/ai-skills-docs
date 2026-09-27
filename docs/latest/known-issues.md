---
sidebar_position: 6
id: known-issues
title: Known Issues
---

# Known Issues

This page lists known issues that may affect your experience with ai-skills. Most are caused by upstream dependencies and are tracked accordingly.

## Current Issues

There are no known open issues at the moment.

## Resolved Issues

### Installed Skills Contained a `.git` Directory

:::tip Resolved in 2.11.0
This issue was fixed in `2.11.0`. It is kept here for anyone still running an earlier version.
:::

When a skill's `SKILL.md` was at the root of its Git repository, `install`, `update` and marketplace installs from `search` copied the whole temporary clone into the installed skill, including its `.git` directory. `install` from a local folder and `sync` also copied any `.git` found in the skill folder. Skills in a subfolder of a repository were not affected through Git sources, because the subfolder has no `.git`.

**What happened:**

- The installed skill became a Git repository of its own and carried the source repository's objects and refs.
- In a project that commits its skills (e.g. `.claude/skills/`), `git add` warned `adding embedded git repository` and recorded the skill as a gitlink (mode `160000`) instead of its files, so anyone who cloned the project got an empty skill directory.
- `update` put `.git` back every time it replaced the skill, even after it had been removed by hand, and `sync` spread it to the other agents' skill directories.

**Cause:** The skill folder copy in ai-skills did not leave out `.git`.

**Fix:** Since `2.11.0`, every skill folder copy leaves out `.git`. Skills installed by 2.10.0 or earlier have no recorded version, so the first `aiskills update` after upgrading replaces them with a copy without `.git`. Skills without source metadata are not updated, so remove their `.git` by hand or re-install them.

If Git already recorded a skill as a gitlink, remove the gitlink from the index once `.git` is gone, and add the files again:

```bash
git rm --cached .claude/skills/my-skill
git add .claude/skills/my-skill
```

**Workaround (versions before 2.11.0):** Delete `.git` from the installed skill directory after every `install`, `update` or `sync`.

### Multi-Choice List Rendering

:::tip Resolved in 2.7.0
This issue was fixed in `2.7.0`. It is kept here for anyone still running an earlier version.
:::

When ai-skills displayed an interactive multi-choice list - used in commands like `install`, `list`, `read`, `remove`, `sync`, and `search` - the list could render incorrectly if there was not enough vertical space below the cursor in the terminal.

:::info NOTE
This was a **display-only** issue. ai-skills itself continued to work correctly - selections, installs, and other operations were unaffected. Only the on-screen rendering looked wrong.
:::

**When it happened:** The bad rendering was triggered when an **error message** was displayed below the multi-choice list - for example, when you pressed Enter without selecting anything and the prompt showed `Please select at least one, or press Ctrl+C to cancel.` If no error message ever appeared, the list rendered correctly.

<video controls width="100%" muted playsinline>
  <source src="https://github.com/user-attachments/assets/add8f341-9043-4c24-8d10-0ed6f22315b5" type="video/mp4" />
  Your browser does not support the video tag.
</video>

**Cause:** An upstream issue in the [cue4s](https://github.com/neandertech/cue4s) library, tracked at [neandertech/cue4s#58](https://github.com/neandertech/cue4s/issues/58).

**Workaround (versions before 2.7.0):** Clear the terminal first and start using `ai-skills` from a freshly cleared screen. Common shortcuts to clear:

- **macOS (iTerm2 / Terminal.app):** `Cmd+K`
- **Linux / Windows terminals:** `Ctrl+L` (or run `clear`)

If you still saw the issue, increasing the terminal window height so there was enough empty space below the prompt also helped.
