package util;

import java.util.Random;

/**
 * Utility class for generating pseudo-random names and values.
 */
public final class RandomDataUtil {

    private static final String[] FIRST_NAMES = {
        "Juan", "Ana", "Carlos", "María", "Pedro", "Luisa",
        "Sofía", "Andrés", "Camila", "Julián", "Valentina", "Miguel"
    };

    private static final String[] LAST_NAMES = {
        "Pérez", "Gómez", "Rodríguez", "López", "Martínez", "García",
        "Ramírez", "Torres", "Castro", "Hernández", "Vargas", "Morales"
    };

    private static final String[] PRODUCT_NAMES = {
        "Arroz", "Leche", "Pan", "Huevos", "Azúcar", "Café",
        "Aceite", "Sal", "Queso", "Chocolate", "Galletas", "Jugo"
    };

    private static final String[] DOCUMENT_TYPES = { "CC", "TI", "CE" };

    private static final Random RANDOM = new Random();

    private RandomDataUtil() {
    }

    public static String randomFirstName() {
        return FIRST_NAMES[RANDOM.nextInt(FIRST_NAMES.length)];
    }

    public static String randomLastName() {
        return LAST_NAMES[RANDOM.nextInt(LAST_NAMES.length)];
    }

    public static String randomProductName(int index) {
        if (index < PRODUCT_NAMES.length) {
            return PRODUCT_NAMES[index];
        }
        return "Producto" + (index + 1);
    }

    public static String randomDocumentType() {
        return DOCUMENT_TYPES[RANDOM.nextInt(DOCUMENT_TYPES.length)];
    }

    public static double randomPrice() {
        return 1000 + (9000 * RANDOM.nextDouble());
    }

    public static int randomQuantity() {
        return 1 + RANDOM.nextInt(20);
    }

    public static int randomProductIndex(int maxExclusive) {
        return RANDOM.nextInt(maxExclusive);
    }
}
