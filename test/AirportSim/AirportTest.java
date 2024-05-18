package AirportSim;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class AirportTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 59})
    void getHour(int min)
    {
        assertEquals(0, Airport.getHour(min));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 60, 120})
    void getMin(int min)
    {
        assertEquals(0, Airport.getMin(min));
    }
}