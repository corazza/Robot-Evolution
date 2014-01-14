/**
 * 
 */
package org.rtevo.main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.rtevo.common.Configuration;

/**
 * 
 * Supports creating multiple instances when.
 * 
 * @author Jan Corazza
 */

/*
 * Weight is a property of all objects, joints and body have weights, make them
 * extend something, evolve this weight
 * 
 * Think about: no legs or anything like that, just random polygons with weight
 * and joints
 */
public class RobotEvolution {
    private Configuration config;
    private ExecutorService workerPool;

    public RobotEvolution(Configuration config) {
        this.config = config;

        workerPool = Executors.newCachedThreadPool();
    }

    // TODO think about threads - do they actually pay of in the way we're
    // implementing them?

    // TODO thread priorities

    public void start() {
        // IF GUI ENABLED:
        // 1. Create initial population.
        // 2. Divide that population to parallelSimulation simulations.
        // 3. submit() (parallelSimulation-1) simulations to workerPool.
        // 4. Keep the remaining simulation in an update-render loop. If it is
        // done display an overlay "waiting for other threads" or pause
        // rendering - POSSIBLE: call get() on all Futures that you kept from
        // submit() calls, this will pause the current thread without wasting
        // resources.
        // 5. In that loop check all simulations if they are finished. If not
        // GOTO 4, else GOTO 6.
        // 6. get() all results from Callable simulations, create a new
        // generation, GOTO 2.

        // IF GUI DISABLED:
        // all the same, without update-render loop, just call() the Callable
        // simulation, then get() all other thread's Results from their Future.
    }
}
