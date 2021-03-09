package edu.neu.csye7125.webapp.Entity.Alert;

import edu.neu.csye7125.webapp.Entity.User.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="alert")
public class Alert {

    @Transient
    public static final String[] categories = new String[] {"new", "top", "best"};

    @Transient
    public static final Set<String> CATEGORY = new HashSet<>(Arrays.asList(categories));

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "category")
    private String category;

    @Column(name = "keyword")
    private String keyword;

    @Column(name = "expiry")
    private Timestamp expiry;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

}
