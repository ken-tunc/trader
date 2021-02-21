package org.kentunc.trader.domain.model.order

enum class TimeInForce {

    // Good 'Til Canceled
    GTC,

    // Immediate or Cancel
    IOC,

    // Fill or Kill
    FOK;
}
