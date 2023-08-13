//package com.blog.models;
//
//import org.hibernate.annotations.GenericGenerator;
//
//import javax.persistence.*;
//import java.io.Serializable;
//
//public class BookSubscriber implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//
//    @Id
//    @GeneratedValue(generator = "uuid")
//    @GenericGenerator(name = "uuid", strategy = "uuid2")
//    private String id;
//
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "subscriber_id")
//    private User user;
//}
