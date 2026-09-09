package org.example.model;

public enum Institution {
    // Écoles
    ENS("ENS — École Normale Supérieure", "École"),
    ENI("ENI — École Nationale d'Informatique", "École"),
    EMIT("EMIT — École de Management et d'Innovation Technologique", "École"),
    
    // Facultés
    FACULTE_SCIENCES("Faculté des Sciences", "Faculté"),
    FDSP("FDSP — Faculté de Droit et des Sciences Politiques", "Faculté"),
    FLSH("FLSH — Faculté des Lettres et des Sciences Humaines", "Faculté"),
    FACULTE_MEDECINE("Faculté de Médecine", "Faculté"),
    EGSS_MCI("Faculté EGSS-MCI — Économie, Gestion et Sciences Sociales de Développement", "Faculté"),
    
    // Instituts
    ISTE("ISTE — Institut des Sciences et Techniques de l'Environnement", "Institut"),
    ISST("ISST — Institut Supérieur des Sciences et Technologies", "Institut"),
    INSTITUT_CONFUCIUS("Institut Confucius", "Institut");

    private final String label;
    private final String category;

    Institution(String label, String category) {
        this.label = label;
        this.category = category;
    }

    public String getLabel() {
        return label;
    }

    public String getCategory() {
        return category;
    }
    
    public boolean isEcole() {
        return "École".equals(category);
    }
    
    public boolean isFaculte() {
        return "Faculté".equals(category);
    }
    
    public boolean isInstitut() {
        return "Institut".equals(category);
    }
}
