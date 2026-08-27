# UI test plan

The `test-ui` skill runs each case in a fresh Bkxss process. Inputs are sent in
order, followed by `bye`. Expected output includes only the responses to the
listed inputs, not the greeting or farewell. Persistence tests use the shared
relative data file and should be run with a clean `data/bkxss.txt` first.

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

## Test case: Create deadline task

Aim: Verify that a deadline command preserves its description and due date.

### Inputs

```text
deadline return book /by Sunday
```

### Expected output

```text
Got it. I've added this task:
[D][ ] return book (by: Sunday)
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
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
list
```

### Expected output

```text
Got it. I've added this task:
[T][ ] borrow book
Now you have 1 tasks in the list.
Got it. I've added this task:
[D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
Got it. I've added this task:
[E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
Here are the tasks in your list:
1.[T][ ] borrow book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
```

## Test case: Delete a task

Aim: Verify that delete removes the correct task and renumbers the remaining list.

### Inputs

```text
todo borrow book
deadline return book /by Sunday
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
[D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
Got it. I've added this task:
[E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
Noted. I've removed this task:
  [D][ ] return book (by: Sunday)
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
