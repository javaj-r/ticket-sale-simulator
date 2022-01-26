package com.javid.control.controllers;

import com.javid.config.Configurable;
import com.javid.control.CinemaController;
import com.javid.domain.Cinema;
import com.javid.domain.Privilege;
import com.javid.domain.User;
import com.javid.repository.CinemaRepository;
import com.javid.repository.PrivilegeRepository;
import com.javid.repository.UserRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author javid
 * Created on 1/5/2022
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CinemaControllerImpl implements CinemaController, Configurable {

    UserRepository userRepository = CONFIG.getUserRepository();
    CinemaRepository cinemaRepository = CONFIG.getCinemaRepository();
    PrivilegeRepository privilegeRepository = CONFIG.getPrivilegeRepository();

    private static class Singleton {
        private static final CinemaController INSTANCE = new CinemaControllerImpl();
    }

    public static CinemaController getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public Cinema register(Cinema cinema) {
        cinema.setEnabled(false);
        setPrivileges(cinema);
        User user = userRepository.save(cinema);

        if (user.isNew())
            return cinema;

        cinema.setId(user.getId());
        cinema.setBalance(0L);

        return cinemaRepository.save(cinema);
    }

    @Override
    public List<Cinema> getUnconfirmedCinemas() {
        return cinemaRepository.findAll()
                .stream()
                .filter(cinema -> !cinema.isEnabled())
                .toList();
    }

    @Override
    public Cinema getCinema(Cinema cinema) {
        Cinema fetched = cinemaRepository.find(cinema);
        if (fetched.isNew())
            return fetched;

        List<Privilege> list = privilegeRepository.findAllByUserId(fetched.getId())
                .stream().toList();

        fetched.setPrivileges(list);

        return fetched;
    }

    private void setPrivileges(Cinema cinema) {
        Privilege logIn = new Privilege();
        logIn.setName(Privilege.Values.CINEMA_LOGIN);
        Privilege createTicket = new Privilege();
        createTicket.setName(Privilege.Values.CREATE_TICKET);
        Privilege deleteTicket = new Privilege();
        deleteTicket.setName(Privilege.Values.DELETE_TICKET);

        cinema.getPrivileges().add(logIn);
        cinema.getPrivileges().add(createTicket);
        cinema.getPrivileges().add(deleteTicket);
    }
}
