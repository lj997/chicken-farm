package com.chicken.farm.service.impl;

import com.chicken.farm.service.AiConfigService;
import com.chicken.farm.service.MarketPriceService;
import com.chicken.farm.vo.AiConfigVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MarketPriceServiceImpl implements MarketPriceService {

    private static final Logger logger = LoggerFactory.getLogger(MarketPriceServiceImpl.class);

    @Autowired
    private AiConfigService aiConfigService;

    @Override
    public BigDecimal getMarketPrice(String breed) {
        AiConfigVO config = aiConfigService.getCurrentConfig();
        
        if (config == null || !StringUtils.hasText(config.getApiKey())) {
            throw new RuntimeException("AI_CONFIG_NOT_SET");
        }

        try {
            String baseUrl = config.getBaseUrl();
            if (baseUrl.endsWith("/")) {
                baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
            }

            OpenAiApi openAiApi = new OpenAiApi(baseUrl, config.getApiKey());

            OpenAiChatOptions options = OpenAiChatOptions.builder()
                    .withModel(config.getModelVersion())
                    .withTemperature(0.3)
                    .build();

            ChatModel chatModel = new OpenAiChatModel(openAiApi, options);

            String promptText = String.format(
                    "请查询当前中国市场上%s品种的活鸡市场售卖价格（每斤价格）。" +
                    "请只回复一个数字价格，不要有其他文字说明，单位为元/斤。" +
                    "例如：15.5",
                    breed
            );

            Prompt prompt = new Prompt(promptText);
            String response = chatModel.call(prompt).getResult().getOutput().getText();
            
            logger.info("AI 市场价格查询 - 品种: {}, 响应: {}", breed, response);

            BigDecimal price = parsePriceFromResponse(response);
            
            if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("获取市场价格失败，请稍后重试");
            }

            return price.setScale(2, RoundingMode.HALF_UP);

        } catch (RuntimeException e) {
            if ("AI_CONFIG_NOT_SET".equals(e.getMessage())) {
                throw e;
            }
            logger.error("获取市场价格失败", e);
            throw new RuntimeException("获取市场价格失败：" + e.getMessage());
        }
    }

    private BigDecimal parsePriceFromResponse(String response) {
        if (response == null || response.trim().isEmpty()) {
            return null;
        }

        response = response.trim();

        Pattern pattern = Pattern.compile("(\\d+(?:\\.\\d+)?)");
        Matcher matcher = pattern.matcher(response);
        
        if (matcher.find()) {
            try {
                return new BigDecimal(matcher.group(1));
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }
}
