package com.greenbuilding.demo.dao;

import com.greenbuilding.demo.api.BaseDAO;
import com.greenbuilding.demo.entity.Trainer;


public class TrainerDAO extends BaseDAO <Trainer, Integer>{

    public TrainerDAO() {
        super(Trainer.class);
    }
}
