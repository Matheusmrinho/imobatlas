export type Region = 'NORTE' | 'NORDESTE' | 'CENTRO_OESTE' | 'SUDESTE' | 'SUL';
export const REGIONS: {value: Region; label: string}[] = [
  {value:'NORTE',label:'Norte'}, {value:'NORDESTE',label:'Nordeste'}, {value:'CENTRO_OESTE',label:'Centro-Oeste'},
  {value:'SUDESTE',label:'Sudeste'}, {value:'SUL',label:'Sul'}
];
export interface Property { id:number; name:string; city:string; state:string; country:string; region:Region; }
export interface Fund { ticker:string; name:string; segment:string; currency:string; country:string;
  price:number; monthlyDistribution:number; asOf:string; source:string; demo:boolean; properties:Property[]; }
export interface SimulationRequest { ticker:string; initialInvestment:number; monthlyContribution:number; months:number; reinvestDistributions:boolean; }
export interface SimulationMonth { month:number; units:number; contributed:number; distribution:number; cash:number;
  withdrawn:number; investedValue:number; totalValue:number; }
export interface SimulationResult { ticker:string; currency:string; demo:boolean; asOf:string; fixedPrice:number;
  fixedMonthlyDistribution:number; assumptions:string; totalContributed:number; totalDistributions:number;
  finalValue:number; timeline:SimulationMonth[]; }
