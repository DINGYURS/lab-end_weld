package sz.lab.service.weld;

import sz.lab.dto.system.PageResultDTO;
import sz.lab.dto.weld.WeldDefectInsertDTO;
import sz.lab.dto.weld.WeldDefectUpdateDTO;
import sz.lab.dto.weld.WeldDefectPageQueryDTO;
import sz.lab.entity.weld.Weld;

public interface WeldDefectService {

    /**
     * 焊缝缺陷信息查询
     *
     * @param weldDefectPageQueryDTO
     * @return
     */
    PageResultDTO pageQuery(WeldDefectPageQueryDTO weldDefectPageQueryDTO);

    /**
     * 插入焊缝缺陷信息
     * @param weldDefectInsertDTO
     */
    void insert(WeldDefectInsertDTO weldDefectInsertDTO);

    /**
     * 修改焊缝缺陷信息
     * @param weldDefectUpdateDTO
     */
    void update(WeldDefectUpdateDTO weldDefectUpdateDTO);
}
