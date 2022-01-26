package com.javid.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author javid
 * Created on 1/1/2022
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class Ticket extends BaseEntity {

    private String movieName;
    private String date;
    private String time;
    private Integer number;
    private Integer sold;
    private Long price;
    private Cinema cinema;
    private List<Customer> customers = new ArrayList<>();

    public Ticket(String movieName, String date, String time, Integer number, Long price) {
        this.movieName = movieName;
        this.date = date;
        this.time = time;
        this.number = number;
        this.price = price;
        this.sold = 0;
    }

    @Override
    public Ticket setId(Integer id) {
        super.setId(id);
        return this;
    }

    @Override
    public String toString() {
        return "[ " +
               "MovieName: " + movieName +
               ", ShowDate: " + date +
               ", ShowTime: " + time +
               ", TotalNumber: " + number +
               ", Sold: " + sold +
               ", Price: " + price +
               " ]";
    }
}
