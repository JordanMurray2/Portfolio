/**
 * This interface is required to be implemented by all hardware that interacts with the system clock.
 */

export interface ClockListener {

    // Notify all clock attached hardware when a pulse occurs
    pulse() : void
}
