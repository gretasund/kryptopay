package com.javamaster.model;

import javax.persistence.*;


@Entity
public class LogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(optional = false)
    private Payment payment;

}
