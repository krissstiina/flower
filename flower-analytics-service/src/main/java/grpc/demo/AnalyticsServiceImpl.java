package grpc.demo;

import grpc.flowers.BouquetAnalyticsRequest;
import grpc.flowers.BouquetAnalyticsResponse;
import grpc.flowers.BouquetAnalyticsServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Random;

@GrpcService
public class AnalyticsServiceImpl extends BouquetAnalyticsServiceGrpc.BouquetAnalyticsServiceImplBase {

    private final Random random = new Random();

    @Override
    public void calculateBouquetPopularity(
            BouquetAnalyticsRequest request,
            StreamObserver<BouquetAnalyticsResponse> responseObserver) {

        // Вычисляем тренд продаж
        double trendFactor = request.getTotalSales() == 0 ? 0 :
                ((double) request.getRecentSales() / request.getTotalSales());

        // Основная формула популярности
        int score = (int) (
                request.getTotalSales() * 0.3 +
                        request.getRecentSales() * 0.5 +
                        request.getUserLikes() * 0.2 +
                        trendFactor * 10 // бонус за растущий тренд
        );

        if (score > 100) score = 100;
        if (score < 0) score = 0;

        // Определяем уровень популярности на русском
        String level;
        if (score >= 85) level = "Очень высокий";
        else if (score >= 70) level = "Высокий";
        else if (score >= 40) level = "Средний";
        else if (score >= 20) level = "Низкий";
        else level = "Очень низкий";

        // Случайные советы для продвижения (на русском)
        String[] promotionTips = {
                "Активнее продвигайте в социальных сетях.",
                "Предлагайте комбинированные наборы.",
                "Выделите самые популярные цвета.",
                "Сотрудничайте с блогерами и инфлюенсерами.",
                "Запустите акции с ограниченным сроком."
        };
        String promotion = promotionTips[random.nextInt(promotionTips.length)];

        String[] stockTips = {
                "Поддерживайте высокий запас для удовлетворения спроса.",
                "Снижайте запас, чтобы избежать избытка.",
                "Держите в наличии самые продаваемые букеты.",
                "Периодически обновляйте инвентарь, чтобы избежать порчи."
        };
        String stock = stockTips[random.nextInt(stockTips.length)];

        String[] pricingTips = {
                "Сохраняйте текущие цены.",
                "Предлагайте скидки на наборы.",
                "Увеличьте цену на премиальные букеты.",
                "Проводите сезонные акции и специальные предложения."
        };
        String pricing = pricingTips[random.nextInt(pricingTips.length)];

        // Формируем структурированную рекомендацию в JSON-подобном виде
        String recommendation = String.format(
                "{\"promotion\":\"%s\", \"stock\":\"%s\", \"pricing\":\"%s\"}",
                promotion, stock, pricing
        );

        // Формируем ответ
        BouquetAnalyticsResponse response = BouquetAnalyticsResponse.newBuilder()
                .setBouquetId(request.getBouquetId())
                .setPopularityScore(score)
                .setPopularityLevel(level)
                .setRecommendation(recommendation)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
