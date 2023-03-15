import React from "react";
import { useForm } from "react-hook-form";

function SignIn() {
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm();
  const onSubmit = (data) => console.log(data);

  const responseMessage = (response) => {
    console.log(response);
  };
  const errorMessage = (error) => {
    console.log(error);
  };

  return (
    <div className="">
      <div className="w-1/3 m-auto px-14 mt-20 py-10 rounded-md shadow-md text-text_color">
        <form
          onSubmit={handleSubmit(onSubmit)}
          className=" flex flex-col gap-10 "
        >
          <h1 className="text-4xl font-semibold self-center">Sign In</h1>
          <h2 className="text-2xl font-normal self-center">
            Welcome to JobFinder
          </h2>
          <div className="flex flex-col gap-2">
            <label className="text-lg" htmlFor="email">
              Email
            </label>
            <input
              type="email"
              autoFocus
              {...register("email", {
                required: true,
              })}
              className="border border-slate-200 focus:outline-none p-2 rounded-md"
            />
            {errors.email && <p>This field is required</p>}
          </div>

          <div className="flex flex-col gap-2">
            <label className="text-lg" htmlFor="password">
              Password
            </label>
            <input
              type="password"
              autoFocus
              {...register("password", { required: true })}
              className="border border-slate-200 focus:outline-none p-2 rounded-md "
            />
            {errors.password && <p>This field is required</p>}
          </div>

          <input
            type="submit"
            className=" bg-background_color hover:bg-background_color_hover p-2 w-full text-lg rounded-md text-white font-normal self-center"
          />
        </form>

        <div>
          <p className="mt-5 text-right hover:text-blue-600">
            Forgot your password?
          </p>
        </div>

        <div>
          <p className="mt-5 text-center">Or</p>
        </div>
      </div>
    </div>
  );
}
export default SignIn;
