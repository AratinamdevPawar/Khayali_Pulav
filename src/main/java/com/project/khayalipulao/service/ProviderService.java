package com.project.khayalipulao.service;
import com.project.khayalipulao.model.MenuItem;
import com.project.khayalipulao.model.User;
import com.project.khayalipulao.repository.MenuRepository;
import com.project.khayalipulao.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProviderService {

	@Autowired
    private MenuRepository menuRepository;

    @Autowired
    private UserRepository userRepository;

    public void addMenuItem(MenuItem menu, int providerId) {
        Optional<User> provider = userRepository.findById(providerId);
        provider.ifPresent(menu::setProvider);
        menuRepository.save(menu);
    }


    public List<MenuItem> getTodayMenu() {
        List<MenuItem> menu = menuRepository.findByDateAdded(LocalDate.now());
        return menu;
    }
}
