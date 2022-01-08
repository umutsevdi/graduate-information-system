create or replace function createFormControl()
    returns trigger as
$body$
begin
    if (
        new."from" = new."to"
        -- or new."from" not in (select id from "user" where "user".cv_path is null)
        ) then
        raise exception 'exception: invalid arguments while creating a job entry form';
    end if;
    update "user" set open2work = true where "user".id = new.id;
    return new;
end ;
$body$
    language plpgsql volatile;

create trigger "create_form"
    before insert
    on "form"
    for each row
execute procedure createFormControl();

