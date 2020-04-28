package entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@NamedQuery(name = "RenameMe.deleteAllRows", query = "DELETE from RenameMe")
@XmlRootElement
public class RenameMe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    public RenameMe() {
    }
        
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    // TODO, delete this class, or rename to an Entity class that makes sense for what you are about to do
    // Delete EVERYTHING below if you decide to use this class, it's dummy data used for the initial demo
    private String dummyStr1;
    private String dummyStr2;

    public RenameMe(String dummyStr1, String dummyStr2) {
        this.dummyStr1 = dummyStr1;
        this.dummyStr2 = dummyStr2;
    }

    public String getDummyStr1() {
        return dummyStr1;
    }

    public void setDummyStr1(String dummyStr1) {
        this.dummyStr1 = dummyStr1;
    }

    public String getDummyStr2() {
        return dummyStr2;
    }

    public void setDummyStr2(String dummyStr2) {
        this.dummyStr2 = dummyStr2;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.dummyStr1);
        hash = 83 * hash + Objects.hashCode(this.dummyStr2);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RenameMe other = (RenameMe) obj;
        if (!Objects.equals(this.dummyStr1, other.dummyStr1)) {
            return false;
        }
        if (!Objects.equals(this.dummyStr2, other.dummyStr2)) {
            return false;
        }
        return true;
    }
    
    
    

   
}
