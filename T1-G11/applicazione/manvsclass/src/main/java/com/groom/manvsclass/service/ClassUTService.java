package com.groom.manvsclass.service;

import com.groom.manvsclass.model.entity.ClassUTEntity;

public interface ClassUTService {

    public Long updateClassUT(String oldName, ClassUTEntity newContent);

    public ClassUTEntity deleteClassUT(String name);
}
