package com.polarnick.javahomework.task7.tasks;

/**
 * Date: 02.04.14 at 19:59
 *
 * @author Nickolay Polyarniy aka PolarNick
 */
public class TaskWithArgument<R, V> {

    private Task<R, V> task;
    private V arg;

    public TaskWithArgument(Task<R, V> task, V arg) {
        this.task = task;
        this.arg = arg;
    }

    public R call() {
        return task.run(arg);
    }

    @Override
    public String toString() {
        return "TaskWithArgument{" +
                "arg=" + arg +
                '}';
    }
}
