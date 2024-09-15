package sz.lab.controller.weld;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sz.lab.dto.system.OperateResultDTO;
import sz.lab.dto.system.PageResultDTO;
import sz.lab.dto.weld.WeldDefectInsertDTO;
import sz.lab.dto.weld.WeldDefectUpdateDTO;
import sz.lab.dto.weld.WeldDefectPageQueryDTO;
import sz.lab.service.weld.WeldDefectService;

import javax.annotation.Resource;

@Validated
@RequestMapping("/weld")
@RestController
@Slf4j
public class WeldDefectController {
    @Resource
    private WeldDefectService weldDefectService;

    /**
     * 焊缝缺陷信息查询
     * @param weldDefectPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public OperateResultDTO pageQuery(WeldDefectPageQueryDTO weldDefectPageQueryDTO){
        log.info("焊缝缺陷信息查询： {}", weldDefectPageQueryDTO);
        OperateResultDTO operateResultDTO = new OperateResultDTO();
        try {
            PageResultDTO pageResultDTO = weldDefectService.pageQuery(weldDefectPageQueryDTO);
            operateResultDTO.setResult(pageResultDTO);
            operateResultDTO.setSuccess(true);
        } catch (Exception e) {
            operateResultDTO.setSuccess(false);
            log.error(e.getMessage());
        }
        return operateResultDTO;
    }

    /**
     * 插入焊缝缺陷信息
     * @param weldDefectInsertDTO
     * @return
     */
    @PostMapping
    public OperateResultDTO insert(@ModelAttribute  WeldDefectInsertDTO weldDefectInsertDTO) {
        log.info("插入焊缝缺陷信息：{}", weldDefectInsertDTO);
        OperateResultDTO operateResultDTO = new OperateResultDTO();
        try {
            weldDefectService.insert(weldDefectInsertDTO);
            operateResultDTO.setSuccess(true);
        } catch (Exception e) {
            operateResultDTO.setSuccess(false);
            log.error(e.getMessage());
        }
        return operateResultDTO;
    }

    /**
     * 修改焊缝缺陷信息
     * @param weldDefectUpdateDTO
     * @return
     */
    @PutMapping
    public OperateResultDTO update(@RequestBody WeldDefectUpdateDTO weldDefectUpdateDTO) {
        log.info("修改焊缝缺陷信息：{}", weldDefectUpdateDTO);
        OperateResultDTO operateResultDTO = new OperateResultDTO();
        try {
            weldDefectService.update(weldDefectUpdateDTO);
            operateResultDTO.setSuccess(true);
        } catch (Exception e) {
            operateResultDTO.setSuccess(false);
            log.error(e.getMessage());
        }
        return operateResultDTO;
    }
}
