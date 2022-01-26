package com.javid.control;

import com.javid.domain.Cinema;

import java.util.List;

/**
 * @author javid
 * Created on 1/5/2022
 */
public interface CinemaController {

    Cinema register(Cinema cinema);

    List<Cinema> getUnconfirmedCinemas();

    Cinema getCinema(Cinema cinema);
}
