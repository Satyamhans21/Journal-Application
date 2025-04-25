package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {
@Autowired
    private JournalEntryRepository journalEntryRepository;
@Autowired
private UserService userService;
@Transactional
public void saveEntry(JournalEntry myEntry, String userName) {
    try {
        User user = userService.findByUserName(userName);


        JournalEntry saved = journalEntryRepository.save(myEntry);
        user.getJournalEntries().add(saved);
        userService.saveUser(user);
    } catch (Exception e){
        System.out.println(e);
        throw new RuntimeException("an error occured while saving the entry",e);
    }
}
public void saveEntry(JournalEntry myEntry){



    journalEntryRepository.save(myEntry);

}

public List<JournalEntry> getAll(){
    return journalEntryRepository.findAll();
}

public Optional<JournalEntry> getById(ObjectId id){
    return journalEntryRepository.findById(id);
}
@Transactional
public boolean deleteById(ObjectId id, String userName){
    boolean removed=false;
    try {
        User user=userService.findByUserName(userName);
         removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
        if(removed) {
            userService.saveUser(user);
            journalEntryRepository.deleteById(id);
        }
    }catch (Exception e){
        System.out.println(e);
        throw new RuntimeException("an error occured while deleting the entry",e);
    }
    return removed;

}
}
