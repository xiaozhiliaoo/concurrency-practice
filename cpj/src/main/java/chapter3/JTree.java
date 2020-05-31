package chapter3;

import EDU.oswego.cs.dl.util.concurrent.FJTask;

/**
 * @author lili
 * @date 2020/5/30 22:06
 * @description
 * @notes
 */
abstract class JTree extends FJTask {
    volatile double maxDiff; // for convergence check
}
