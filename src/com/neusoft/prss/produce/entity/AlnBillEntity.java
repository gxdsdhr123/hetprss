/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年1月8日 下午1:55:05
 *@author:neusoft
 *@version:[v1.0]
 */
package com.neusoft.prss.produce.entity;

public class AlnBillEntity {
    private String id;

    private String fltid;

    private String flightNumber;

    private String actType;

    private String aircraftNumber;

    private String operator;

    private String operatorName;

    private String signatory;

    private String createDate;

    private String actstandCode;

    private String flightDate;

    private String etd;

    private String eta;

    private String ata;

    private String atd;

    private String inFlightNumber;

    private String outFlightNumber;

    private String inFltid;

    private String outFltid;

    private String fltAttrCode;

    private String arrival;

    private String transit;

    private String nightstop;

    private String departure;

    private String returntoramp;

    private String others;

    private String towingFrom;

    private String towingTo;

    private String oilUpliftOne;

    private String oilUpliftTwo;

    private String oilUpliftThree;

    private String oilUpliftFour;

    private String oilUpliftApu;

    private String csdoilUpliftOne;

    private String csdoilUpliftTwo;

    private String csdoilUpliftThree;

    private String csdoilUpliftFour;

    private String csdoilUpliftApu;

    private String hydraulicoilUpliftOne;

    private String hydraulicoilUpliftTwo;

    private String hydraulicoilUpliftThree;

    private String hydraulicoilUpliftFour;

    private String hydraulicoilUpliftApu;

    private String engineoilStock;

    private String engineoilQty;

    private String hydStock;

    private String hydQty;

    private String apuoilStock;

    private String apuoilQty;

    private String aircraftPushout;

    private String aircraftTowing;

    private String waterServicing;

    private String toiletServicing;

    private String groundPowerUnit;

    private String airConditiongUnit;

    private String gasTurbineStarterUnit;

    private String oxygenCharging;

    private String nitrogenCharging;

    private String maintenanceSteps;

    private String maintPlatform;

    private String wheelJacks;

    private String equipmentTowTug;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFltid() {
        return fltid;
    }

    public void setFltid(String fltid) {
        this.fltid = fltid;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber == null ? null : flightNumber.trim();
    }

    public String getActType() {
        return actType;
    }

    public void setActType(String actType) {
        this.actType = actType == null ? null : actType.trim();
    }

    public String getAircraftNumber() {
        return aircraftNumber;
    }

    public void setAircraftNumber(String aircraftNumber) {
        this.aircraftNumber = aircraftNumber == null ? null : aircraftNumber.trim();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName == null ? null : operatorName.trim();
    }

    public String getSignatory() {
        return signatory;
    }

    public void setSignatory(String signatory) {
        this.signatory = signatory == null ? null : signatory.trim();
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getActstandCode() {
        return actstandCode;
    }

    public void setActstandCode(String actstandCode) {
        this.actstandCode = actstandCode == null ? null : actstandCode.trim();
    }

    public String getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(String flightDate) {
        this.flightDate = flightDate == null ? null : flightDate.trim();
    }

    public String getEtd() {
        return etd;
    }

    public void setEtd(String etd) {
        this.etd = etd == null ? null : etd.trim();
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta == null ? null : eta.trim();
    }

    public String getAta() {
        return ata;
    }

    public void setAta(String ata) {
        this.ata = ata == null ? null : ata.trim();
    }

    public String getAtd() {
        return atd;
    }

    public void setAtd(String atd) {
        this.atd = atd == null ? null : atd.trim();
    }

    public String getInFlightNumber() {
        return inFlightNumber;
    }

    public void setInFlightNumber(String inFlightNumber) {
        this.inFlightNumber = inFlightNumber == null ? null : inFlightNumber.trim();
    }

    public String getOutFlightNumber() {
        return outFlightNumber;
    }

    public void setOutFlightNumber(String outFlightNumber) {
        this.outFlightNumber = outFlightNumber == null ? null : outFlightNumber.trim();
    }

    public String getInFltid() {
        return inFltid;
    }

    public void setInFltid(String inFltid) {
        this.inFltid = inFltid == null ? null : inFltid.trim();
    }

    public String getOutFltid() {
        return outFltid;
    }

    public void setOutFltid(String outFltid) {
        this.outFltid = outFltid == null ? null : outFltid.trim();
    }

    public String getFltAttrCode() {
        return fltAttrCode;
    }

    public void setFltAttrCode(String fltAttrCode) {
        this.fltAttrCode = fltAttrCode == null ? null : fltAttrCode.trim();
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival == null ? null : arrival.trim();
    }

    public String getTransit() {
        return transit;
    }

    public void setTransit(String transit) {
        this.transit = transit == null ? null : transit.trim();
    }

    public String getNightstop() {
        return nightstop;
    }

    public void setNightstop(String nightstop) {
        this.nightstop = nightstop == null ? null : nightstop.trim();
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure == null ? null : departure.trim();
    }

    public String getReturntoramp() {
        return returntoramp;
    }

    public void setReturntoramp(String returntoramp) {
        this.returntoramp = returntoramp == null ? null : returntoramp.trim();
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others == null ? null : others.trim();
    }

    public String getTowingFrom() {
        return towingFrom;
    }

    public void setTowingFrom(String towingFrom) {
        this.towingFrom = towingFrom == null ? null : towingFrom.trim();
    }

    public String getTowingTo() {
        return towingTo;
    }

    public void setTowingTo(String towingTo) {
        this.towingTo = towingTo == null ? null : towingTo.trim();
    }

    public String getOilUpliftOne() {
        return oilUpliftOne;
    }

    public void setOilUpliftOne(String oilUpliftOne) {
        this.oilUpliftOne = oilUpliftOne == null ? null : oilUpliftOne.trim();
    }

    public String getOilUpliftTwo() {
        return oilUpliftTwo;
    }

    public void setOilUpliftTwo(String oilUpliftTwo) {
        this.oilUpliftTwo = oilUpliftTwo == null ? null : oilUpliftTwo.trim();
    }

    public String getOilUpliftThree() {
        return oilUpliftThree;
    }

    public void setOilUpliftThree(String oilUpliftThree) {
        this.oilUpliftThree = oilUpliftThree == null ? null : oilUpliftThree.trim();
    }

    public String getOilUpliftFour() {
        return oilUpliftFour;
    }

    public void setOilUpliftFour(String oilUpliftFour) {
        this.oilUpliftFour = oilUpliftFour == null ? null : oilUpliftFour.trim();
    }

    public String getOilUpliftApu() {
        return oilUpliftApu;
    }

    public void setOilUpliftApu(String oilUpliftApu) {
        this.oilUpliftApu = oilUpliftApu == null ? null : oilUpliftApu.trim();
    }

    public String getCsdoilUpliftOne() {
        return csdoilUpliftOne;
    }

    public void setCsdoilUpliftOne(String csdoilUpliftOne) {
        this.csdoilUpliftOne = csdoilUpliftOne == null ? null : csdoilUpliftOne.trim();
    }

    public String getCsdoilUpliftTwo() {
        return csdoilUpliftTwo;
    }

    public void setCsdoilUpliftTwo(String csdoilUpliftTwo) {
        this.csdoilUpliftTwo = csdoilUpliftTwo == null ? null : csdoilUpliftTwo.trim();
    }

    public String getCsdoilUpliftThree() {
        return csdoilUpliftThree;
    }

    public void setCsdoilUpliftThree(String csdoilUpliftThree) {
        this.csdoilUpliftThree = csdoilUpliftThree == null ? null : csdoilUpliftThree.trim();
    }

    public String getCsdoilUpliftFour() {
        return csdoilUpliftFour;
    }

    public void setCsdoilUpliftFour(String csdoilUpliftFour) {
        this.csdoilUpliftFour = csdoilUpliftFour == null ? null : csdoilUpliftFour.trim();
    }

    public String getCsdoilUpliftApu() {
        return csdoilUpliftApu;
    }

    public void setCsdoilUpliftApu(String csdoilUpliftApu) {
        this.csdoilUpliftApu = csdoilUpliftApu == null ? null : csdoilUpliftApu.trim();
    }

    public String getHydraulicoilUpliftOne() {
        return hydraulicoilUpliftOne;
    }

    public void setHydraulicoilUpliftOne(String hydraulicoilUpliftOne) {
        this.hydraulicoilUpliftOne = hydraulicoilUpliftOne == null ? null : hydraulicoilUpliftOne.trim();
    }

    public String getHydraulicoilUpliftTwo() {
        return hydraulicoilUpliftTwo;
    }

    public void setHydraulicoilUpliftTwo(String hydraulicoilUpliftTwo) {
        this.hydraulicoilUpliftTwo = hydraulicoilUpliftTwo == null ? null : hydraulicoilUpliftTwo.trim();
    }

    public String getHydraulicoilUpliftThree() {
        return hydraulicoilUpliftThree;
    }

    public void setHydraulicoilUpliftThree(String hydraulicoilUpliftThree) {
        this.hydraulicoilUpliftThree = hydraulicoilUpliftThree == null ? null : hydraulicoilUpliftThree.trim();
    }

    public String getHydraulicoilUpliftFour() {
        return hydraulicoilUpliftFour;
    }

    public void setHydraulicoilUpliftFour(String hydraulicoilUpliftFour) {
        this.hydraulicoilUpliftFour = hydraulicoilUpliftFour == null ? null : hydraulicoilUpliftFour.trim();
    }

    public String getHydraulicoilUpliftApu() {
        return hydraulicoilUpliftApu;
    }

    public void setHydraulicoilUpliftApu(String hydraulicoilUpliftApu) {
        this.hydraulicoilUpliftApu = hydraulicoilUpliftApu == null ? null : hydraulicoilUpliftApu.trim();
    }

    public String getEngineoilStock() {
        return engineoilStock;
    }

    public void setEngineoilStock(String engineoilStock) {
        this.engineoilStock = engineoilStock == null ? null : engineoilStock.trim();
    }

    public String getEngineoilQty() {
        return engineoilQty;
    }

    public void setEngineoilQty(String engineoilQty) {
        this.engineoilQty = engineoilQty == null ? null : engineoilQty.trim();
    }

    public String getHydStock() {
        return hydStock;
    }

    public void setHydStock(String hydStock) {
        this.hydStock = hydStock == null ? null : hydStock.trim();
    }

    public String getHydQty() {
        return hydQty;
    }

    public void setHydQty(String hydQty) {
        this.hydQty = hydQty == null ? null : hydQty.trim();
    }

    public String getApuoilStock() {
        return apuoilStock;
    }

    public void setApuoilStock(String apuoilStock) {
        this.apuoilStock = apuoilStock == null ? null : apuoilStock.trim();
    }

    public String getApuoilQty() {
        return apuoilQty;
    }

    public void setApuoilQty(String apuoilQty) {
        this.apuoilQty = apuoilQty == null ? null : apuoilQty.trim();
    }

    public String getAircraftPushout() {
        return aircraftPushout;
    }

    public void setAircraftPushout(String aircraftPushout) {
        this.aircraftPushout = aircraftPushout == null ? null : aircraftPushout.trim();
    }

    public String getAircraftTowing() {
        return aircraftTowing;
    }

    public void setAircraftTowing(String aircraftTowing) {
        this.aircraftTowing = aircraftTowing == null ? null : aircraftTowing.trim();
    }

    public String getWaterServicing() {
        return waterServicing;
    }

    public void setWaterServicing(String waterServicing) {
        this.waterServicing = waterServicing == null ? null : waterServicing.trim();
    }

    public String getToiletServicing() {
        return toiletServicing;
    }

    public void setToiletServicing(String toiletServicing) {
        this.toiletServicing = toiletServicing == null ? null : toiletServicing.trim();
    }

    public String getGroundPowerUnit() {
        return groundPowerUnit;
    }

    public void setGroundPowerUnit(String groundPowerUnit) {
        this.groundPowerUnit = groundPowerUnit == null ? null : groundPowerUnit.trim();
    }

    public String getAirConditiongUnit() {
        return airConditiongUnit;
    }

    public void setAirConditiongUnit(String airConditiongUnit) {
        this.airConditiongUnit = airConditiongUnit == null ? null : airConditiongUnit.trim();
    }

    public String getGasTurbineStarterUnit() {
        return gasTurbineStarterUnit;
    }

    public void setGasTurbineStarterUnit(String gasTurbineStarterUnit) {
        this.gasTurbineStarterUnit = gasTurbineStarterUnit == null ? null : gasTurbineStarterUnit.trim();
    }

    public String getOxygenCharging() {
        return oxygenCharging;
    }

    public void setOxygenCharging(String oxygenCharging) {
        this.oxygenCharging = oxygenCharging == null ? null : oxygenCharging.trim();
    }

    public String getNitrogenCharging() {
        return nitrogenCharging;
    }

    public void setNitrogenCharging(String nitrogenCharging) {
        this.nitrogenCharging = nitrogenCharging == null ? null : nitrogenCharging.trim();
    }

    public String getMaintenanceSteps() {
        return maintenanceSteps;
    }

    public void setMaintenanceSteps(String maintenanceSteps) {
        this.maintenanceSteps = maintenanceSteps == null ? null : maintenanceSteps.trim();
    }

    public String getMaintPlatform() {
        return maintPlatform;
    }

    public void setMaintPlatform(String maintPlatform) {
        this.maintPlatform = maintPlatform == null ? null : maintPlatform.trim();
    }

    public String getWheelJacks() {
        return wheelJacks;
    }

    public void setWheelJacks(String wheelJacks) {
        this.wheelJacks = wheelJacks == null ? null : wheelJacks.trim();
    }

    public String getEquipmentTowTug() {
        return equipmentTowTug;
    }

    public void setEquipmentTowTug(String equipmentTowTug) {
        this.equipmentTowTug = equipmentTowTug == null ? null : equipmentTowTug.trim();
    }
}
