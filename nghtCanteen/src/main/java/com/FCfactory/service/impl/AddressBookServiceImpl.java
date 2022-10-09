package com.FCfactory.service.impl;

import com.FCfactory.entity.AddressBook;
import com.FCfactory.mapper.AddressBookMapper;
import com.FCfactory.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
