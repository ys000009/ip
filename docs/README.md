# Bkxss User Guide

Bkxss is a personal task assistant that stores todos, deadlines, and events.

## Finding free times

Use `findfree` to find the earliest continuous free slot inside a search range:

`findfree HOURS /from START /to END`

`HOURS` must be a positive whole number. `START` and `END` use the
`yyyy-MM-dd HHmm` format. For example:

```text
findfree 4 /from 2026-09-12 0900 /to 2026-09-13 1800
```

Bkxss compares the range with all saved events and reports the earliest slot
that is at least four hours long. Todos and deadlines do not occupy time, and
an event still occupies time when it is marked as done.

New events require valid complete dates, with their start strictly before their end:

```text
event project meeting /from 2026-09-12 1000 /to 2026-09-12 1200
```

Older events that use free-text times, such as `Mon 2pm`, can still be loaded
and displayed. Bkxss asks you to replace such an event with complete dates
before calculating free time, because its position on the calendar is unknown.

The search range is continuous and may cross midnight. Bkxss does not assume
working or sleeping hours, so include only the hours you want searched.

## Adding deadlines

Use `deadline DESCRIPTION /by DATE` to add a task with a due date. Dates use
the `yyyy-MM-dd HHmm` format.

```text
deadline return book /by 2026-09-15 1800
```

## Other commands

- `todo DESCRIPTION`: add a todo
- `event DESCRIPTION /from START /to END`: add an event
- `list`: show all tasks
- `find KEYWORD`: find tasks by description
- `mark NUMBER` and `unmark NUMBER`: change a task's completion status
- `delete NUMBER`: remove a task
- `bye`: exit Bkxss

## Input errors and saved data

Leading/trailing spaces, repeated spaces, and tabs are accepted and normalized.
Supply each named parameter exactly once in the documented order. `list` and
`bye` accept no arguments. Task numbers must refer to an existing task; durations
must be positive whole numbers. Invalid commands leave the task list unchanged.
Task descriptions support Unicode and punctuation, but cannot contain `|`, line
breaks, or control characters because these would corrupt saved records.

Tasks with the same type, description, and dates are duplicates even if one is
completed. Descriptions are case-sensitive for duplicate detection. Different
dates or task types are allowed. Delete an existing task before re-adding it.

A missing data file is created on the first successful change. Failed saves
leave the task list and previous saved file unchanged; check folder/file
permissions and disk space before retrying. Saves require a filesystem that
supports atomic file replacement.

If saved records are damaged or duplicated, valid records are still loaded.
Changes are blocked to protect the original file. Back up and repair
`data/bkxss.txt` (or fix its permissions), then restart the application. The
startup console reports invalid line numbers. Read-only commands remain usable.
