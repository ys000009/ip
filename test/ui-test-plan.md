# UI test plan

The `test-ui` skill runs each case in a fresh Bkxss process. Inputs are sent in
order, followed by `bye`. Expected output includes only the responses to the
listed inputs, not the greeting or farewell. Each case uses a temporary working directory so existing task data stays intact.

## GUI checks: A-BetterGui

Run the GUI from a temporary working directory with a fresh data file.

1. At startup, confirm that the command field has focus and Send is disabled.
   Spaces alone must keep Send disabled; Enter must not create empty messages.
   The subtitle should read `Your task butler, at your service!`, and the title,
   controls, focus state, and user messages should use the blue visual theme.
2. Enter `todo borrow book`, `mark 2`, `mark 1`, `list`, `todo`, then `list`.
   Commands should be compact and right-aligned; replies should be full-width cards.
   Each command should show the circular cat avatar on the right. Each Bkxss reply
   should show the circular dog avatar on the left.
   Only `mark 2` and `todo` should have a red card headed CHECK YOUR COMMAND.
   Both lists must contain exactly one completed task.
3. Add `todo OhNo!! ERROR :( --> investigate`. Its successful reply must use normal styling.
4. Add a task with a long description and run `list`. Resize the window to its
   minimum size and then to 900 × 700. Text should wrap without truncation or
   horizontal scrolling, and the input and Send button should remain usable.
5. Send enough commands to overflow the conversation. Each new reply should scroll
   into view; scrolling upward should still let you read earlier replies.
   The pale-blue `chatbot_background` world map should fill the conversation viewport
   without tiling, while every message remains easy to read.
6. Use both Enter and Send. Focus should return to the input after sending.
7. Enter `bye`. The farewell should remain visible and both input and Send should
   be disabled. Relaunch and confirm the saved task state is preserved.

## Test case: Create todo task

Aim: Verify that a todo command creates and displays a Todo task.

### Inputs

```text
todo borrow book
```

### Expected output

```text
Got it. I've added this task:
[T][ ] borrow book
Now you have 1 tasks in the list.
```

## Test case: Mark and unmark a task

Aim: Verify that mark and unmark update a task's status and reject duplicate operations.

### Inputs

```text
todo borrow book
mark 1
mark 1
unmark 1
unmark 1
```

### Expected output

```text
Got it. I've added this task:
[T][ ] borrow book
Now you have 1 tasks in the list.
Nice! I've marked this task as done:
  [T][X] borrow book
OhNo!! ERROR :( --> this task is already marked as done!
OK, I've marked this task as not done yet:
  [T][ ] borrow book
OhNo!! ERROR :( --> this task is already unmarked!
```

## Test case: Interleaved valid and invalid mark operations

Aim: Verify that invalid mark/unmark commands do not corrupt task state.

### Inputs

```text
todo borrow book
mark 2
mark 1
mark abc
list
unmark 1
unmark 99
list
```

### Expected output

```text
Got it. I've added this task:
[T][ ] borrow book
Now you have 1 tasks in the list.
OhNo!! ERROR :( --> there is no task numbered 2.
Nice! I've marked this task as done:
  [T][X] borrow book
OhNo!! ERROR :( --> please provide a task number. Use: mark/unmark/delete NUMBER
Here are the tasks in your list:
1.[T][X] borrow book
OK, I've marked this task as not done yet:
  [T][ ] borrow book
OhNo!! ERROR :( --> there is no task numbered 99.
Here are the tasks in your list:
1.[T][ ] borrow book
```

## Test case: Invalid commands

Aim: Verify that missing todo descriptions, an accidental trailing space after list, and unknown commands show helpful errors without ending the program.

### Inputs

```text
todo
list 
blah
```

### Expected output

```text
OhNo!! ERROR :( --> The description of a todo cannot be empty.
OhNo!! ERROR :( --> omg! you've entered an empty space at the end of the "list" accidentally
OhNo!! ERROR :( --> I'm sorry, but I don't know what that means :-(
```

## Test case: Find tasks by description keyword

Aim: Verify that find returns matching tasks case-insensitively, preserves task display formats, and rejects an empty keyword without changing the task list.

### Inputs

```text
todo read book
deadline return book /by 2019-12-02 1800
event project meeting /from Mon 2pm /to 4pm
find BOOK
find
list
```

### Expected output

```text
Got it. I've added this task:
[T][ ] read book
Now you have 1 tasks in the list.
Got it. I've added this task:
[D][ ] return book (by: Dec 02 2019 18:00)
Now you have 2 tasks in the list.
Got it. I've added this task:
[E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
Here are the matching tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Dec 02 2019 18:00)
OhNo!! ERROR :( --> please provide a keyword to search for. Use: find KEYWORD
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Dec 02 2019 18:00)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
```

## Test case: Create deadline task

Aim: Verify that a deadline command preserves its description and due date.

### Inputs

```text
deadline return book /by 2019-12-02 1800
```

### Expected output

```text
Got it. I've added this task:
[D][ ] return book (by: Dec 02 2019 18:00)
Now you have 1 tasks in the list.
```

## Test case: Create event task

Aim: Verify that an event command preserves its description, start, and end times.

### Inputs

```text
event project meeting /from Mon 2pm /to 4pm
```

### Expected output

```text
Got it. I've added this task:
[E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 1 tasks in the list.
```

## Test case: List polymorphic tasks

Aim: Verify that the Task array lists Todo, Deadline, and Event objects using their own output formats.

### Inputs

```text
todo borrow book
deadline return book /by 2019-12-02 1800
event project meeting /from Mon 2pm /to 4pm
list
```

### Expected output

```text
Got it. I've added this task:
[T][ ] borrow book
Now you have 1 tasks in the list.
Got it. I've added this task:
[D][ ] return book (by: Dec 02 2019 18:00)
Now you have 2 tasks in the list.
Got it. I've added this task:
[E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
Here are the tasks in your list:
1.[T][ ] borrow book
2.[D][ ] return book (by: Dec 02 2019 18:00)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
```

## Test case: Delete a task

Aim: Verify that delete removes the correct task and renumbers the remaining list.

### Inputs

```text
todo borrow book
deadline return book /by 2019-12-02 1800
event project meeting /from Mon 2pm /to 4pm
delete 2
list
delete 5
```

### Expected output

```text
Got it. I've added this task:
[T][ ] borrow book
Now you have 1 tasks in the list.
Got it. I've added this task:
[D][ ] return book (by: Dec 02 2019 18:00)
Now you have 2 tasks in the list.
Got it. I've added this task:
[E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
Noted. I've removed this task:
  [D][ ] return book (by: Dec 02 2019 18:00)
Now you have 2 tasks in the list.
Here are the tasks in your list:
1.[T][ ] borrow book
2.[E][ ] project meeting (from: Mon 2pm to: 4pm)
OhNo!! ERROR :( --> there is no task numbered 5.
```

## Test case: Save and reload tasks

Aim: Verify that changed tasks are saved and loaded by a later chatbot startup.

### Inputs

```text
todo remember persistence
mark 1
```

### Expected output

```text
Got it. I've added this task:
[T][ ] remember persistence
Now you have 1 tasks in the list.
Nice! I've marked this task as done:
  [T][X] remember persistence
```

## Test case: Reject invalid deadline without changing task state

Aim: Verify that an invalid date is rejected and does not add a corrupted deadline task.

### Inputs

```text
deadline return book /by 2019-02-30 1800
deadline return book /by 2019-02-28 1800
list
```

### Expected output

```text
OhNo!! ERROR :( --> please provide a valid deadline in yyyy-MM-dd HHmm format, e.g. 2019-12-02 1800
Got it. I've added this task:
[D][ ] return book (by: Feb 28 2019 18:00)
Now you have 1 tasks in the list.
Here are the tasks in your list:
1.[D][ ] return book (by: Feb 28 2019 18:00)
```

## Test case: Find the earliest free time

Aim: Verify that findfree finds an exact-sized gap and reports when no gap is long enough.

### Inputs

```text
event lecture /from 2026-09-12 0900 /to 2026-09-12 1000
event workshop /from 2026-09-12 1200 /to 2026-09-12 1500
findfree 2 /from 2026-09-12 0900 /to 2026-09-12 1700
findfree 3 /from 2026-09-12 0900 /to 2026-09-12 1700
```

### Expected output

```text
Got it. I've added this task:
[E][ ] lecture (from: 2026-09-12 0900 to: 2026-09-12 1000)
Now you have 1 tasks in the list.
Got it. I've added this task:
[E][ ] workshop (from: 2026-09-12 1200 to: 2026-09-12 1500)
Now you have 2 tasks in the list.
The earliest 2-hour free slot is:
  Sep 12 2026 10:00 to Sep 12 2026 12:00
I couldn't find a 3-hour free slot between Sep 12 2026 09:00 and Sep 12 2026 17:00.
```

## Test case: Reject invalid free-time inputs without changing tasks

Aim: Verify that invalid searches and event ranges do not corrupt state, and legacy events are handled safely.

### Inputs

```text
event lecture /from 2026-09-12 0900 /to 2026-09-12 1000
findfree 0 /from 2026-09-12 0900 /to 2026-09-12 1700
findfree 2 /from 2026-02-30 0900 /to 2026-09-12 1700
event impossible /from 2026-09-12 1200 /to 2026-09-12 1100
findfree 1 /from 2026-09-12 1700 /to 2026-09-12 0900
event vague meeting /from Mon 2pm /to 4pm
findfree 2 /from 2026-09-12 0900 /to 2026-09-12 1700
delete 2
findfree 2 /from 2026-09-12 0900 /to 2026-09-12 1700
list
```

### Expected output

```text
Got it. I've added this task:
[E][ ] lecture (from: 2026-09-12 0900 to: 2026-09-12 1000)
Now you have 1 tasks in the list.
OhNo!! ERROR :( --> please provide the duration as a positive whole number of hours.
OhNo!! ERROR :( --> please provide valid search dates in yyyy-MM-dd HHmm format, e.g. 2026-09-12 0900
OhNo!! ERROR :( --> an event's start must be before its end.
OhNo!! ERROR :( --> the free-time search start must be before its end.
Got it. I've added this task:
[E][ ] vague meeting (from: Mon 2pm to: 4pm)
Now you have 2 tasks in the list.
OhNo!! ERROR :( --> event 2 does not use yyyy-MM-dd HHmm dates. Re-add it with dated /from and /to values.
Noted. I've removed this task:
  [E][ ] vague meeting (from: Mon 2pm to: 4pm)
Now you have 1 tasks in the list.
The earliest 2-hour free slot is:
  Sep 12 2026 10:00 to Sep 12 2026 12:00
Here are the tasks in your list:
1.[E][ ] lecture (from: 2026-09-12 0900 to: 2026-09-12 1000)
```


## Test case: GUI command sequence preserves state after errors

Aim: Verify the GUI smoke-test command sequence also preserves task state in the console, including task descriptions containing error text.

### Inputs

```text
todo borrow book
mark 2
mark 1
list
todo
list
todo OhNo!! ERROR :( --> investigate
list
```

### Expected output

```text
Got it. I've added this task:
[T][ ] borrow book
Now you have 1 tasks in the list.
OhNo!! ERROR :( --> there is no task numbered 2.
Nice! I've marked this task as done:
  [T][X] borrow book
Here are the tasks in your list:
1.[T][X] borrow book
OhNo!! ERROR :( --> The description of a todo cannot be empty.
Here are the tasks in your list:
1.[T][X] borrow book
Got it. I've added this task:
[T][ ] OhNo!! ERROR :( --> investigate
Now you have 2 tasks in the list.
Here are the tasks in your list:
1.[T][X] borrow book
2.[T][ ] OhNo!! ERROR :( --> investigate
```
