package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @GetMapping("list")
    public Result<List<AddressBook>> getAddressBookList() {
        List<AddressBook> list = addressBookService.queryAddressBook();
        return Result.success(list);
    }

    @GetMapping("default")
    public Result<AddressBook> getDefaultAddressBook() {
        AddressBook addressBook = addressBookService.queryDefaultAddressBook();
        return Result.success(addressBook);
    }

    @GetMapping("/{id}")
    public Result<AddressBook> getAddressBookById(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    @PutMapping("default")
    public Result<Object> setDefaultAddressBook(@RequestBody AddressBook addressBook){
        addressBookService.setDefaultAddressBook(addressBook);
        return Result.success();
    }

    @DeleteMapping
    public Result<Object> deleteAddressBookById(Long id){
        addressBookService.removeAddressBookById(id);
        return Result.success();
    }

    @PostMapping
    public Result<Object> addAddressBook(@RequestBody AddressBook addressBook){
        addressBookService.saveAddressBook(addressBook);
        return Result.success();
    }

    @PutMapping
    public Result<Object> editAddressBook(@RequestBody AddressBook addressBook){
        addressBookService.updateAddressBookById(addressBook);
        return Result.success();
    }

}
