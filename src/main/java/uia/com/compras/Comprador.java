package uia.com.compras;

import java.util.ArrayList;
import java.util.List;

public class Comprador {

    private int clasificacion = 0;

    public void hazSolicitudOrdenCompra(PeticionOrdenCompra miPeticionOC) {
        validaExistencia(miPeticionOC);
    }

    private void validaExistencia(PeticionOrdenCompra miPeticionOC) {
        for (int i = 0; i < miPeticionOC.getItems().size(); i++)
        {
            validaUso((PeticionOrdenCompra) miPeticionOC.getItems().get(i), i);
        }

        for (int i = 0; i < miPeticionOC.getItems().size(); i++)
        {
            if ((i % 2) == 0)
                miPeticionOC.getItems().remove(i);
        }
    }

    private void validaUso(PeticionOrdenCompra miPeticionOC, int i) {
        switch (i % 3) {
            case 0:
                miPeticionOC.setClasificacion(0);
                break;
            case 1:
                miPeticionOC.setClasificacion(1);
                break;
            case 2:
                miPeticionOC.setClasificacion(2);
                break;
        }
    }
}
