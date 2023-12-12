package com.gift.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gift.domain.Project;
import com.gift.domain.Vo.FunctionVo;
import com.gift.mapper.ProjectMapper;
import com.gift.service.ProjectService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.giftorg.analyze.dao.FunctionESDao;
import org.giftorg.analyze.entity.Function;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;


    @Override
    public Project testGetById(Integer id) {

        return projectMapper.selectById(id);
    }

    /**
     * 向ES查询并返回结果代码对象
     * @param keys
     * @return
     */
    @Override
    public List<FunctionVo> getByCode(String keys) {
//        HttpHost httpHost = HttpHost.create("http://elasticsearch:9200");
//        RestClientBuilder builder = RestClient.builder(httpHost);
//        RestHighLevelClient client = new RestHighLevelClient(builder);

//        try {
//            List<Function> retrieval = functionESDao.retrieval(keys);   // TODO 返回的是ahao的common模块下的实体类
//            return retrieval;
//        } catch (Exception exception) {
//            log.error("查询出错，{}",exception);
//            exception.printStackTrace();
//        }

//        try {
//            List<Function> retrieval = elasticsearch.retrieval("gift_function", "description", "怎么连接数据库", "embedding", Function.class);
//            retrieval.forEach(System.out::println);
//        } catch (Exception exception) {
//
//            log.error("Gift查询异常,{}",exception);
//        }

//        try {
//            List<Function> functions = FunctionESDao.retrieval("怎么连接数据库");
//            functions.forEach(System.out::println);
//        } catch (Exception exception) {
//            log.info("Gift查询失败{}",exception);
//        }

        FunctionESDao functionESDao = new FunctionESDao();
        try {
            List<Function> functions = functionESDao.retrieval("怎么连接数据库");

            //转换为Vo
            FunctionVo functionVo = new FunctionVo();
            ArrayList<FunctionVo> functionVos = new ArrayList<>();

            functions.forEach(function -> {
                BeanUtils.copyProperties(function,functionVo);
                functionVos.add(functionVo);
            });

            functionVos.forEach(System.out::println);

            return functionVos;

        } catch (Exception exception) {
            log.info("查询出错,{}",exception);
        }

        return null;
    }


}
