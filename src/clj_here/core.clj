(ns clj-here.core)

(defn break [locals num-args]
  (swank.core/invoke-debugger
   locals
   (Exception. (str "CLJ-HERE BREAK: After argument #" num-args))
   nil))

(defn create-locals [args]
  (reduce #(assoc %1 (str "ARG" (count %1)) %2) {} (reverse args)))

(defn eval-arg [[argx & argxs :as args] arg]
  (let [locals (create-locals args)]
    (cond
     (= arg :dbg-prn-last) (do (prn argx) args)
     (= arg :break) (do (break locals (count args)) args)
     :else (conj args arg))))

(defmacro debug [f & args]
  (let [new-arg-values (gensym "new-arg-values")
        locals (gensym "locals")]
    `(let [~locals (swank.core/local-bindings)
           ~new-arg-values (reduce ~eval-arg '() [~@args])]
       (apply ~f (reverse ~new-arg-values)))))

(defmacro debug-block [& lines]
  `(do ~@(map (fn [line]
         (if (seq? line)
           `(debug ~@line)
           line))
       lines)))