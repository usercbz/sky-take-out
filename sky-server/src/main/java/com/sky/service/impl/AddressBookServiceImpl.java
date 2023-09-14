package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
    @Override
    public List<AddressBook> queryAddressBook() {
        return list(new LambdaQueryWrapper<AddressBook>()
                .eq(AddressBook::getUserId, BaseContext.getCurrentId()));
    }

    @Override
    public AddressBook queryDefaultAddressBook() {

        return getOne(new LambdaQueryWrapper<>(AddressBook.class)
                .eq(AddressBook::getUserId, BaseContext.getCurrentId())
                .eq(AddressBook::getIsDefault, 1));
    }

    @Override
    @Transactional
    public void setDefaultAddressBook(AddressBook addressBook) {
        //将原来的默认地址修改
        update(new LambdaUpdateWrapper<>(AddressBook.class)
                .eq(AddressBook::getIsDefault, 1)
                .set(AddressBook::getIsDefault, 0));

        addressBook.setIsDefault(1);
        updateById(addressBook);
    }

    @Override
    public void removeAddressBookById(Long id) {
        removeById(id);
    }

    @Override
    public void saveAddressBook(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        save(addressBook);
    }

    @Override
    public void updateAddressBookById(AddressBook addressBook) {
        updateById(addressBook);
    }
}
