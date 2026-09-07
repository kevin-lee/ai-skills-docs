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
