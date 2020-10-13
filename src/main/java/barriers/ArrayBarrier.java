package barriers;


import java.util.ArrayList;
import java.util.List;

public class ArrayBarrier implements Barrier {

    private final int maxThreadCount;
    private final List<Integer> threadStates = new ArrayList<>();

    public ArrayBarrier(int maxThreadCount) {
        this.maxThreadCount = maxThreadCount;
        clean();
    }

    private void clean() {
        for (int i = 0; i < maxThreadCount; i++) {
            threadStates.add(0);
        }
    }

    @Override
    public void await() {
        int id = Integer.parseInt(Thread.currentThread().getName()); //будем при создании треда помещать его номер в имя
        if(id == 0) {
            threadStates.set(id, 1);
        }
        if (id < maxThreadCount - 1) {
            if(id > 0) {
                while ((threadStates.get(id - 1) != 1)) {}
                threadStates.set(id, 1);
            }
            while ((threadStates.get(id + 1) != 2)) {}
            threadStates.set(id, 2);
            return;
        }

        if (id == maxThreadCount - 1) {
            while ((threadStates.get(id - 1) != 1)) {}
            threadStates.set(id, 2);
        }
    }
}
