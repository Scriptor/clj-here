# clj-here

A simple tool to help debug nested expressions. Takes a function call and lets you insert various debugging-related keywords to inspect the various arguments being passed in the slime repl.

## Usage

We'll use the following function call as the template. It's a simple call to `prn`
and uses an imaginary function `foo` as an argument.

```clojure
(ns clj-here-example)

(prn "hello" (foo :bar :baz))
```

The main function in clj-here is `debug` so we'll need to :use it and transform the call to `foo` to a call to `debug` like this:

```clojure
(ns clj-here-example
  (:use [clj-here :only (debug)]))

(debug prn "hello" (foo :bar :baz))
```

The :dbg-prn-last keyword can be inserted after an argument to print it out.

```clojure
(ns clj-here-example
  (:use [clj-here :only (debug)]))

(debug prn "hello" :dbg-prn-last (foo :bar :baz))
; Will print out "hello" in the repl
```

The :break keyword creates a breakpoint in the middle of a function. It uses sldb and
sets the locals as the all the arguments before it.

```clojure
(ns clj-here-example
  (:use [clj-here :only (debug))))

(debug prn "hello" "bye" :break (foo :bar :baz))
```

In sldb this will result in:

```
Backtrace:
  0:             core.clj:6 clj-here.core/break
      Locals:
        ARG0 = "hello"
        ARG1 = "bye"
```

## License

Copyright (C) 2012 Tamreen Khan

Distributed under the Eclipse Public License, the same as Clojure.
