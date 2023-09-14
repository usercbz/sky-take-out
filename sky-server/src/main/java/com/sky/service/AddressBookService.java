package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService extends IService<AddressBook> {

    List<AddressBook> queryAddressBook();

    AddressBook queryDefaultAddressBook();

    void setDefaultAddressBook(AddressBook addressBook);

    void removeAddressBookById(Long id);

    void saveAddressBook(AddressBook addressBook);

    void updateAddressBookById(AddressBook addressBook);
}
