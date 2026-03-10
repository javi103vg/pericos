package com.example.moviles.model.dto;

public record MovilFiltroDTO(
        Double precioMin,     
        Double precioMax,      
        String marca,          
        Integer ramMin,        
        Integer ramMax,       
        Boolean nfc,          
        String pantallaTecnologia 
) {}