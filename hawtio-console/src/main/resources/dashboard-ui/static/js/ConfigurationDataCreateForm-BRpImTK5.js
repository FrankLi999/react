import{u as c,j as e,C as m,L as i}from"./index-DGiImij6.js";import{A as u,C as p,z as d}from"./config-data-schema-BgYT3WrU.js";import{R as r}from"./Row-Ciq4gX0y.js";import{C as n,a as t}from"./Col-C3qAUvrC.js";const f={"ui:submitButtonOptions":{submitText:"Submit Configuration Data",norender:!1,props:{disabled:!1,className:"btn btn-info"}},application:{"ui:title":"Application","ui:autofocus":!0,"ui:emptyValue":"","ui:autocomplete":"my-camel-integrator"},profile:{"ui:title":"Profile","ui:emptyValue":"","ui:autocomplete":"default","ui:placeholder":"Spring profile"},label:{"ui:title":"Label","ui:emptyValue":"","ui:autocomplete":"master","ui:placeholder":"master"},props:{items:{propKey:{"ui:placeholder":"Property key"},propValue:{"ui:placeholder":"Property value"}}}},g={application:"my-camel-integrator",label:"master",profile:"default",props:[{id:1,propKey:"key",propValue:"value"}]};function b(){const[s]=c(["XSRF-TOKEN"]);async function l(a){console.log("submitted data",a),console.log("submitted for data",a.formData);try{const o={method:"POST",headers:{"Content-Type":"application/json",accept:"application/json","X-XSRF-TOKEN":s["XSRF-TOKEN"]},body:JSON.stringify([a.formData])};await fetch("/my-camel/admin/api/spring-config/configurations",o)}catch(o){console.log(o)}}return e.jsx(e.Fragment,{children:e.jsx(m,{fluid:!0,children:e.jsx(r,{children:e.jsx(n,{md:"12",children:e.jsxs(t,{children:[e.jsxs(t.Header,{children:[e.jsx(t.Title,{as:"h4",children:"Camel Integrator Configurations"}),e.jsx("p",{className:"card-category",children:"Create configurations for an application profile"}),e.jsx("div",{className:"small form-group d-flex align-items-center justify-content-between mt-4 mb-0",children:e.jsx(i,{to:"/integrator/configuration-data",children:"Go to configurations page"})})]}),e.jsx(t.Body,{children:e.jsx(r,{children:e.jsx(n,{md:"12",children:e.jsx("div",{className:"form",children:e.jsx(u,{schema:p,uiSchema:f,formData:g,validator:d,onSubmit:a=>l(a)})})})})}),e.jsx(t.Footer,{className:"card-footer text-center",children:e.jsx("div",{className:"small form-group d-flex align-items-center justify-content-between mt-4 mb-0",children:e.jsx(i,{to:"/integrator/configuration-data",children:"Go to configurations page"})})})]})})})})})}export{b as default};